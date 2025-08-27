#ifndef SYSTEM_MANAGER_H
#define SYSTEM_MANAGER_H

#include <Arduino.h>

class SystemManager{
public:
    SystemManager(int fsrPin, int pressureThreshold, unsigned long shutdownTimeout);
    void setup();
    void update();
    bool isSystemOn() const;

private:
    const int _fsrPin;
    const int _pressureThreshold;
    const unsigned long _shutdownTimeout;
    bool _isSystemOn;
    unsigned long _lastPressureTime; 
};
#endif