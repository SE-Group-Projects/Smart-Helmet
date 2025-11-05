package com.example.smarthelmetapp.data.service


import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

data class HelmetSensorData(
    val temperature: Float,
    val speed: Float,
    val batteryLevel: Float,
    val helmetStatus: String, // "Worn" or "Not Worn"
    val ventStatus: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@SuppressLint("MissingPermission")
class BluetoothService(private val context: Context) {
    private val TAG = "BluetoothService"
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    // Standard UUID for Serial Port Profile (SPP)
    private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private var isConnected = false

    fun isBluetoothSupported(): Boolean = bluetoothAdapter != null

    fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true

    fun hasBluetoothPermission(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    suspend fun connectToDevice(deviceAddress: String): Boolean = withContext(Dispatchers.IO) {
        if (!hasBluetoothPermission()) {
            Log.e(TAG, "Bluetooth permission not granted")
            return@withContext false
        }

        try {
            val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)

            if (device == null) {
                Log.e(TAG, "Device not found")
                return@withContext false
            }

            bluetoothSocket = device.createRfcommSocketToServiceRecord(SPP_UUID)
            bluetoothAdapter?.cancelDiscovery()

            bluetoothSocket?.connect()
            inputStream = bluetoothSocket?.inputStream
            outputStream = bluetoothSocket?.outputStream

            isConnected = true
            Log.d(TAG, "Connected to device: $deviceAddress")
            true
        } catch (e: IOException) {
            Log.e(TAG, "Connection failed: ${e.message}")
            disconnect()
            false
        }
    }

    fun disconnect() {
        try {
            inputStream?.close()
            outputStream?.close()
            bluetoothSocket?.close()
            isConnected = false
            Log.d(TAG, "Disconnected from device")
        } catch (e: IOException) {
            Log.e(TAG, "Error disconnecting: ${e.message}")
        }
    }

    fun getHelmetDataStream(): Flow<HelmetSensorData> = callbackFlow {
        if (!isConnected || inputStream == null) {
            Log.e(TAG, "Not connected to device")
            close()
            return@callbackFlow
        }

        val buffer = ByteArray(1024)

        try {
            while (isConnected) {
                val bytes = inputStream?.read(buffer) ?: 0
                if (bytes > 0) {
                    val data = String(buffer, 0, bytes).trim()
                    Log.d(TAG, "Received data: $data")

                    // Parse data from ESP32
                    // Expected format: "TEMP:25.5,SPEED:45.2,BATTERY:87,STATUS:Worn,VENT:1"
                    val sensorData = parseHelmetData(data)
                    if (sensorData != null) {
                        trySend(sensorData)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error reading data: ${e.message}")
            close(e)
        }

        awaitClose {
            disconnect()
        }
    }

    private fun parseHelmetData(data: String): HelmetSensorData? {
        return try {
            val parts = data.split(",")
            val dataMap = parts.associate {
                val keyValue = it.split(":")
                keyValue[0] to keyValue[1]
            }

            HelmetSensorData(
                temperature = dataMap["TEMP"]?.toFloatOrNull() ?: 0f,
                speed = dataMap["SPEED"]?.toFloatOrNull() ?: 0f,
                batteryLevel = dataMap["BATTERY"]?.toFloatOrNull()?.div(100f) ?: 0f,
                helmetStatus = dataMap["STATUS"] ?: "Unknown",
                ventStatus = dataMap["VENT"] == "1"
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse helmet data: ${e.message}")
            null
        }
    }

    suspend fun sendCommand(command: String): Boolean = withContext(Dispatchers.IO) {
        if (!isConnected || outputStream == null) {
            Log.e(TAG, "Not connected to device")
            return@withContext false
        }

        try {
            outputStream?.write(command.toByteArray())
            outputStream?.flush()
            Log.d(TAG, "Command sent: $command")
            true
        } catch (e: IOException) {
            Log.e(TAG, "Failed to send command: ${e.message}")
            false
        }
    }

    fun getPairedDevices(): List<BluetoothDevice> {
        if (!hasBluetoothPermission()) {
            Log.e(TAG, "Bluetooth permission not granted")
            return emptyList()
        }

        return bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
    }

    fun isConnectedToDevice(): Boolean = isConnected

    companion object {
        // ESP32 device name - change this to match your ESP32 device
        const val ESP32_DEVICE_NAME = "SmartHelmet_ESP32"

        fun getRequiredPermissions(): Array<String> {
            return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            } else {
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
                )
            }
        }
    }
}