#include "BluetoothNotifier.h"

// Use ESP32's HardwareSerial port 1 for the SIM800L
HardwareSerial& SimSerial = Serial1;

BluetoothNotifier::BluetoothNotifier(const char* phoneNumber, int simTxPin, int simRxPin)
    : _phoneNumber(phoneNumber), _simTxPin(simTxPin), _simRxPin(simRxPin) {}

void BluetoothNotifier::setup() {
    // Start the hardware serial port for the SIM800L module
    SimSerial.begin(9600, SERIAL_8N1, _simRxPin, _simTxPin);
    Serial.println("Hardware Serial for SIM800L started.");
    Serial.println("Alert Notifier initialized.");
}

void BluetoothNotifier::sendCollisionAlert(double lat, double lon) {
    String message = "CRASH DETECTED!\n";
    message += "Location:\n";
    message += "https://maps.google.com/?q=";
    message += String(lat, 6);
    message += ",";
    message += String(lon, 6);

    Serial.println("\n--- Preparing to Send SMS Alert ---");
    Serial.println("To: " + String(_phoneNumber));
    Serial.println("Message: " + message);
    Serial.println("-----------------------------------\n");

    Serial.println("Setting SMS text mode...");
    SimSerial.println("AT+CMGF=1");
    delay(1000);

    Serial.println("Sending SMS to: " + String(_phoneNumber));
    SimSerial.print("AT+CMGS=\"");
    SimSerial.print(_phoneNumber);
    SimSerial.println("\"");
    delay(1000);
    
    SimSerial.print(message);
    delay(1000);

    SimSerial.write(26); // Ctrl+Z
    delay(1000);
    Serial.println("SMS Sent!");
    
}

void BluetoothNotifier::sendVoiceAlert(const char* message){
    Serial.print("BT Alert: ");
    Serial.println(message);
}