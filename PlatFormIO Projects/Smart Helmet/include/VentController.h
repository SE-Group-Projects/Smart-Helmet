#ifndef VENT_CONTROLLER_H
#define VENT_CONTROLLER_H

#include <ESP32Servo.h>

class VentController {
    public:
        VentController(int servoPin, float tempThreshold, int openAngle, int closedAngle);
        void setup();
        void update(float currentTemperature); // get the temp..........
        void manualOpen();
        void manualClose(); 

    private:
        Servo _servo;
        int _servoPin;
        float _tempThreshold;
        int _openAngle;
        int _closedAngle;
        bool _isVentOpen;
};

#endif