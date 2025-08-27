#include "SystemManager.h"

SystemManager::SystemManager(int fsrPin, int pressureThreshold, unsigned long shutdownTimeout)
    : _fsrPin(fsrPin), 
      _pressureThreshold(pressureThreshold), 
      _shutdownTimeout(shutdownTimeout),
      _isSystemOn(false), 
      _lastPressureTime(0) {}

void SystemManager::setup(){
    pinMode(_fsrPin, INPUT);
    Serial.println("System Manager Initialized . Waiting for Pressure...");
}

void SystemManager::update(){
    int fsrReading = analogRead(_fsrPin);
    Serial.println("Raw FSR Reading: " + String(fsrReading));
    
    if (fsrReading > _pressureThreshold){
        if (!_isSystemOn){
             _isSystemOn = true;
            Serial.println("---------------------------------");
            Serial.print("Pressure detected! System is ON. (Reading: ");
            Serial.print(fsrReading);
            Serial.println(")");
            Serial.println("---------------------------------");
        }
        _lastPressureTime = millis();
    }else {
        if (_isSystemOn && (millis() - _lastPressureTime > _shutdownTimeout)){
            _isSystemOn = false;
            Serial.println("---------------------------------");
            Serial.println("No pressure for 1 minute. System is SHUTDOWN.");
            Serial.println("---------------------------------");
        }
    }
}
bool SystemManager::isSystemOn() const {
        return _isSystemOn;
    }