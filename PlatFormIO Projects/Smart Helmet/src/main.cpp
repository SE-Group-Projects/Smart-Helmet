#include <Arduino.h>
#include "SystemManager.h"
#include "SensorManager.h"
#include "VentController.h"
#include "CollisionDetector.h"
// #include "BlynkManager.h"
// #include "SpeedMonitor.h"
// #include "BluetoothNotifier.h"

// pinsss....................
const int FSR_PIN = 34;
const int PRESSURE_THRESHOLD = 500;
const unsigned long SHUTDOWN_TIMEOUT = 60000;  //system time out for  1 min..

const int GPS_RX_PIN = 16;
const int GPS_TX_PIN = 17;
const uint8_t LM75_I2C_ADDRESS = 0x48;

const int SERVO_PIN = 13;
const float TEMP_THRESHOLD_HIGH = 30.0; // Open vent if temp > 30°C
const int VENT_OPEN_ANGLE = 90;
const int VENT_CLOSED_ANGLE = 0;


// instances
SystemManager systemManager(FSR_PIN, PRESSURE_THRESHOLD, SHUTDOWN_TIMEOUT);
SensorManager sensorManager(GPS_TX_PIN, GPS_RX_PIN, LM75_I2C_ADDRESS);
VentController ventController(SERVO_PIN, TEMP_THRESHOLD_HIGH, VENT_OPEN_ANGLE, VENT_CLOSED_ANGLE);
CollisionDetector collisionDetector;

void setup(){
  Serial.begin(115200);
  Serial.println("\n\n Smart Helmet System Initializing. \n");

   // Call the setup function for each component
    systemManager.setup();
    sensorManager.setup();
    ventController.setup();
    collisionDetector.setup();

    Serial.println("All systems initialized. Main loop starting.");
}

void loop(){
  // aways update the sytem with the pressure........
  systemManager.update();

  // only if the the system is on the other will run,,,,,,,,,,,,,,,
  if (systemManager.isSystemOn()){
    // read data from the snesors.......
    sensorManager.readSensor();

    // Update the vent based on the new temperature reading.......
    ventController.update(sensorManager.getTemperature());

    // check for collisionDetector
    collisionDetector.check();

    delay(2000);
  }
}