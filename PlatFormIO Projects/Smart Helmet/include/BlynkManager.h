#ifndef BLYNK_MANAGER_H
#define BLYNK_MANAGER_H

#include <BlynkSimpleEsp32.h>

#define VPIN_TEMPERATURE V0
#define VPIN_SPEED       V1
#define VPIN_GPS_MAP     V2
#define VPIN_STATUS_LED  V3

class BlynkManager {
    public:
    BlynkManager(const char* authToken, const char* ssid, const char* password);
    void setup();
    void run();
    void updateSystemStatus(bool isOn);
    void updateSensorData(float temp, double speed, double lat, double lon, bool isGpsValid);
    void sendCollisionNotification();

private:
    const char* _authToken;
    const char* _ssid;
    const char* _password;
};
#endif