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

