#ifndef SENSOR_MANAGER_H
#define SENSOR_MANAGER_H

#include <TinyGPS++.h>
#include <Wire.h>

class SensorManager {
    public:
        SensorManager(int gpsTxPin, int gpsRxPin, uint8_t lm75Address);

        void setup();
        void readSensor();

        float getTemperature() const;
        double getLatitude() const;
        double getLongitude() const;
        double getSpeedKph() const;
        bool isGpsLocationValid() const;

    private:
        // GPS detials...........................
        int _gpsTxPin, _gpsRxPin;
        TinyGPSPlus _gps;

        // LM75 Temarature Sensor 
        uint8_t _lm75Address;
        float _currentTemperature; // Store the last read temperature
};
#endif