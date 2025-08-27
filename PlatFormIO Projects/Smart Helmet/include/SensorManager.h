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
}
#endif