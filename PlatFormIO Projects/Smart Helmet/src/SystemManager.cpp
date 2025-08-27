#include "SystemManager.h"

SystemManager::SystemManager(int fsrPin, int pressureThreshold, int impactThreshold, unsigned long shutdownTimeout)
    : _fsrPin(fsrPin), 
      _pressureThreshold(pressureThreshold), 
      _impactThreshold(impactThreshold),
      _shutdownTimeout(shutdownTimeout),
      _isSystemOn(false), 
      _collisionDetected(false),
      _lastPressureTime(0),
      _lastFsrReading(0) {}

void SystemManager::setup(){
    pinMode(_fsrPin, INPUT);
    _lastFsrReading = 500; 
    Serial.println("System Manager Initialized . Waiting for Pressure...");
}

void SystemManager::update(){
    int currentFsrReading = analogRead(_fsrPin);
    Serial.println("Raw FSR Reading: " + String(currentFsrReading));

    if (currentFsrReading> _pressureThreshold){
        if (!_isSystemOn){
             _isSystemOn = true;
            Serial.println("---------------------------------");
            Serial.print("Pressure detected! System is ON. (Reading: ");
            Serial.print(currentFsrReading);
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

    if (_isSystemOn && !_collisionDetected){
        int pressureSpike = currentFsrReading - _lastFsrReading;
        if (pressureSpike > _impactThreshold) {
            Serial.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            Serial.printf("!!! COLLISION DETECTED !!! Pressure Spike: %d\n", pressureSpike);
            Serial.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            _collisionDetected = true;
        }
    }
}
bool SystemManager::isSystemOn() const {
        return _isSystemOn;
    }