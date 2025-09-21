#define BLYNK_TEMPLATE_ID "TMPL6jliNOslX"
#define BLYNK_TEMPLATE_NAME "Smart Helmet"
#define BLYNK_AUTH_TOKEN "F_CO_TTQUKhbjbPRwxfjGUA5n7Viq0v-"

#include <Arduino.h>
#include <BlynkSimpleEsp32.h> 
#include "SystemManager.h"
#include "SensorManager.h"
#include "VentController.h"
#include "CollisionDetector.h"
//#include "SpeedMonitor.h"
 #include "BluetoothNotifier.h"


char ssid[] = "KushanA14";
char pass[] = "kushankabi1235";

#define VPIN_TEMPERATURE V0
#define VPIN_SPEED V1
#define VPIN_GPS_LAT V2
#define VPIN_GPS_LON V3
#define VPIN_STATUS_LED V4
#define VPIN_COLLISION V10
#define VPIN_VENT_CONTROL V5
#define VPIN_SPEED_OVERRIDE V6

#define SPEED_LIMIT 60.0 // km/h


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

// instances
SystemManager systemManager(FSR_PIN, PRESSURE_THRESHOLD, COLLISION_IMPACT_THRESHOLD, SHUTDOWN_TIMEOUT);
SensorManager sensorManager(GPS_TX_PIN, GPS_RX_PIN, LM75_I2C_ADDRESS);
VentController ventController(SERVO_PIN, TEMP_THRESHOLD_HIGH, VENT_OPEN_ANGLE, VENT_CLOSED_ANGLE);
BluetoothNotifier notifier(EMERGENCY_PHONE_NUMBER, SIM_TX_PIN, SIM_RX_PIN);
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
    Serial.println("setting up blynk");
    Blynk.begin(BLYNK_AUTH_TOKEN, ssid, pass);
    Serial.println("Done");
    Serial.println("All systems initialized. Main loop starting.");
}

void loop(){
  // aways update the sytem with the pressure........
  systemManager.update();

  // only if the the system is on the other will run,,,,,,,,,,,,,,,
  if (systemManager.isSystemOn()){
    Blynk.run();
    // read data from the snesors.......
    sensorManager.readSensor();
    Blynk.virtualWrite(VPIN_STATUS_LED, systemManager.isSystemOn() ? 1 : 0);

    // Update the vent based on the new temperature reading.......                                                 
    ventController.update(sensorManager.getTemperature());

    // send data to the Blynk ....
    if (millis() - lastBlynkUpdateTime > blynkUpdateInterval){
      Blynk.virtualWrite(VPIN_TEMPERATURE, sensorManager.getTemperature());
      Blynk.virtualWrite(VPIN_SPEED, sensorManager.getSpeedKph());
      
      // Overspeed alert
      if (sensorManager.getSpeedKph() > SPEED_LIMIT) {
        notifier.sendVoiceAlert("Overspeed detected! Slow down.");
        Blynk.logEvent("overspeed", "You are driving too fast!");
      }

      // Bend alert (example check near a hardcoded location)
      double bendLat = 6.9271; // Example: Colombo bend point
      double bendLon = 79.8612;
      if (sensorManager.isGpsLocationValid() && sensorManager.getSpeedKph() > 20) {
        double dist = sensorManager.distanceTo(bendLat, bendLon);
        if (dist < 50) { // 50 meters
            notifier.sendVoiceAlert("Bend ahead, please slow down.");
        }
      }

      if (sensorManager.isGpsLocationValid()) {
        Blynk.virtualWrite(VPIN_GPS_LAT, sensorManager.getLatitude());
        Blynk.virtualWrite(VPIN_GPS_LON, sensorManager.getLongitude());
      }
      lastBlynkUpdateTime = millis();
    }
    

    //collision check .............
    if (systemManager.isCollisionDetected()) {
        Blynk.virtualWrite(VPIN_COLLISION, 1);
            if (sensorManager.isGpsLocationValid()) {
                notifier.sendCollisionAlert(
                    sensorManager.getLatitude(),
                    sensorManager.getLongitude()
                );
            } else {
                notifier.sendCollisionAlert(0, 0); 
            }
      systemManager.resetCollision();
      //delay(10000);
    }                            

    delay(2000);
  }
}


 BLYNK_WRITE(VPIN_VENT_CONTROL) {
    int value = param.asInt();   // Read the value from the app (0 or 1)

      if (value == 1) {
        ventController.manualOpen();
        Serial.println("Vent opened via Blynk");
      } else {
        ventController.manualClose();
        Serial.println("Vent closed via Blynk");
      }
    }

    BLYNK_WRITE(VPIN_SPEED_OVERRIDE) {
  double testSpeed = param.asDouble();
  if (testSpeed >= 0) {
    sensorManager.setOverrideSpeed(testSpeed);
    Serial.print("Speed overridden to: ");
    Serial.println(testSpeed);
  } else {
    sensorManager.clearOverrideSpeed();
    Serial.println("Speed override cleared, using GPS again.");
  }
}