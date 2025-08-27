#include <Arduino.h>
#include "SystemManager.h"
#include "SensorManager.h"
#include "VentController.h"
#include "CollisionDetector.h"
#include "BlynkManager.h"
//#include "SpeedMonitor.h"
 #include "BluetoothNotifier.h"

// pinsss....................
const int FSR_PIN = 34;
const int PRESSURE_THRESHOLD = 100;
const int COLLISION_IMPACT_THRESHOLD = 500;
const unsigned long SHUTDOWN_TIMEOUT = 200000;  //system time out for  1 min..

const int GPS_RX_PIN = 16;
const int GPS_TX_PIN = 17;
const uint8_t LM75_I2C_ADDRESS = 0x48;

const int SERVO_PIN = 13;
const float TEMP_THRESHOLD_HIGH = 30.0; // Open vent if temp > 30°C
const int VENT_OPEN_ANGLE = 90;
const int VENT_CLOSED_ANGLE = 0;

// Alert Notifier (SIM800L)
const int SIM_RX_PIN = 26;
const int SIM_TX_PIN = 27;
const char* EMERGENCY_PHONE_NUMBER = "+94702016212";

const char* WIFI_SSID = "KushanA14";
const char* WIFI_PASS = "kushankabi1235";
const char* BLYNK_AUTH_TOKEN = "F_CO_TTQUKhbjbPRwxfjGUA5n7Viq0v-";

// instances
SystemManager systemManager(FSR_PIN, PRESSURE_THRESHOLD, COLLISION_IMPACT_THRESHOLD, SHUTDOWN_TIMEOUT);
SensorManager sensorManager(GPS_TX_PIN, GPS_RX_PIN, LM75_I2C_ADDRESS);
VentController ventController(SERVO_PIN, TEMP_THRESHOLD_HIGH, VENT_OPEN_ANGLE, VENT_CLOSED_ANGLE);
BluetoothNotifier notifier(EMERGENCY_PHONE_NUMBER, SIM_TX_PIN, SIM_RX_PIN);
BlynkManager blynkManager(BLYNK_AUTH_TOKEN, WIFI_SSID, WIFI_PASS);
CollisionDetector collisionDetector;

// Timer for sending data to Blynk to avoid flooding
unsigned long lastBlynkUpdateTime = 0;
const long blynkUpdateInterval = 5000; // 5 sec


void setup(){
  Serial.begin(115200);
  Serial.println("\n\n Smart Helmet System Initializing. \n");

   // Call the setup function for each component
    systemManager.setup();
    sensorManager.setup();
    ventController.setup();
    collisionDetector.setup();
    notifier.setup();
    blynkManager.setup();

    Serial.println("All systems initialized. Main loop starting.");
}

void loop(){
  // aways update the sytem with the pressure........
  systemManager.update();
  blynkManager.updateSystemStatus(systemManager.isSystemOn());

  // only if the the system is on the other will run,,,,,,,,,,,,,,,
  if (systemManager.isSystemOn()){
    blynkManager.run();
    // read data from the snesors.......
    sensorManager.readSensor();

    // Update the vent based on the new temperature reading.......                                                 
    ventController.update(sensorManager.getTemperature());

    // send data to the Blynk ....
    if (millis() - lastBlynkUpdateTime > blynkUpdateInterval){
      blynkManager.updateSensorData(
        sensorManager.getTemperature(),
        sensorManager.getSpeedKph(),
        sensorManager.getLatitude(),
        sensorManager.getLongitude(),
        sensorManager.isGpsLocationValid()
      );
      lastBlynkUpdateTime = millis();
    }

    //collision check .............
    if (systemManager.isCollisionDetected()) {
      blynkManager.sendCollisionNotification();
            if (sensorManager.isGpsLocationValid()) {
                notifier.sendCollisionAlert(
                    sensorManager.getLatitude(),
                    sensorManager.getLongitude()
                );
            } else {
                notifier.sendCollisionAlert(0, 0); 
            }
      systemManager.resetCollision();
      delay(10000);
    }                            

    delay(2000);
  }
}