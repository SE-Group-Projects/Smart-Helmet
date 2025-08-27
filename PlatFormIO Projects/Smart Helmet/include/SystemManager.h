#ifndef SYSTEM_MANAGER_H
#define SYSTEM_MANAGER_H

#include <Arduino.h>

class SystemManager{
public:
    SystemManager(int fsrPin, int pressureThreshold, int impactThreshold,unsigned long shutdownTimeout);
    void setup();
    void update();
    bool isSystemOn() const;

    bool isCollisionDetected() const;
    void restCollision();
private:
    const int _fsrPin;
    const int _pressureThreshold;
    const int _impactThreshold;
    const unsigned long _shutdownTimeout;

    bool _isSystemOn;
    bool _collisionDetected;
    unsigned long _lastPressureTime;
    int _lastFsrReading;
};
#endif