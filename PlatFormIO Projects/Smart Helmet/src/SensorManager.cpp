#include "SensorManager.h"
#include <Arduino.h>

// usp 32 serial port 2 for GPS
HardwareSerial& GpsSerial = Serial2;

SensorManager::SensorManager(int gpsTxPin, int gpsRxPin, uint8_t lm75Address)
    : _gpsTxPin(gpsTxPin), 
      _gpsRxPin(gpsRxPin),
      _lm75Address(lm75Address),
      _currentTemperature(0.0f) {}

void SensorManager::setup() {
    // Start the I2C com with LM75
    Wire.begin();
    Serial.println("I2C bus for LM75 started.");

    // Start the hardware sserial port for the GPS module
    GpsSerial.begin(9600, SERIAL_8N1, _gpsRxPin , _gpsTxPin);
    Serial.println("Hardware Serial for GPS started.");
    Serial.println("Sensor Manager initialized.");
}

void SensorManager::readSensor(){
    // Read GPS data..............
    while(GpsSerial.available() > 0){
        _gps.encode(GpsSerial.read());
    }

    // read the LM75..........
    Wire.beginTransmission(_lm75Address);
    Wire.write(0x00); // point to the tempareture sensor regitoery 
    Wire.endTransmission();

    if (Wire.requestFrom(_lm75Address, (uint8_t)2) == 2) {
        byte msb = Wire.read();
        byte lsb = Wire.read();

        // Combine MSB and LSB into a 16-bit integer
        int16_t rawTemp = (msb << 8) | lsb;
        
        // The LM75 data is left-aligned. We need to shift right to get the actual value.
        // This gives a resolution of 0.125 degrees C.
        rawTemp = rawTemp >> 5;

        // Convert to Celsius
        _currentTemperature = rawTemp * 0.125;
    } else {
        Serial.println("Error: Could not read from LM75 sensor.");
        _currentTemperature = -999.0f; // Indicate an error
    }


    // print the summary.....
    Serial.print("Temp: ");
    Serial.print(getTemperature());
    Serial.print(" C | GPS Fix: ");
    Serial.print(isGpsLocationValid() ? "Yes" : "No");
    Serial.print(" | Lat: ");
    Serial.print(getLatitude(), 6);
    Serial.print(" | Lon: ");
    Serial.print(getLongitude(), 6);
    Serial.print(" | Speed: ");
    Serial.print(getSpeedKph());
    Serial.println(" KPH");
}

// getters ..........
float SensorManager::getTemperature() const {
    return _currentTemperature;
}

double SensorManager::getLatitude(){
    return _gps.location.lat();
}

double SensorManager::getLongitude(){
    return _gps.location.lng();
}

double SensorManager::getSpeedKph(){
    return _gps.speed.kmph();
}

bool SensorManager::isGpsLocationValid() const {
    return _gps.location.isValid();
}



