#include "VentController.h"
#include <Arduino.h>

VentController::VentController(int servoPin, float tempThreshold, int openAngle, int closedAngle)
        : _servoPin(servoPin),
            _tempThreshold(tempThreshold),
            _openAngle(openAngle),
            _closedAngle(closedAngle),
            _isVentOpen(false) {}

void VentController::setup() {
     // Allow allocation of all timers
    ESP32PWM::allocateTimer(0);
    ESP32PWM::allocateTimer(1);
    ESP32PWM::allocateTimer(2);
    ESP32PWM::allocateTimer(3);

    _servo.setPeriodHertz(50); // Standard servo frequency
    _servo.attach(_servoPin, 500, 2500); // Attach the servo on the specified pin

    // Start with the vent closed
    _servo.write(_closedAngle);
    Serial.println("Vent Controller initialized. Vent is closed.");
}

void VentController::update(float currentTemperature) {
    if (currentTemperature > _tempThreshold && !_isVentOpen) {
        // Temperature is high and vent is closed, so open it
        _servo.write(_openAngle);
        _isVentOpen = true;
        Serial.println("  -> Temp high! Opening vent.");
    } else if (currentTemperature <= _tempThreshold && _isVentOpen) {
        // Temperature is low and vent is open, so close it
        _servo.write(_closedAngle);
        _isVentOpen = false;
        Serial.println("  -> Temp low. Closing vent.");
    }

}