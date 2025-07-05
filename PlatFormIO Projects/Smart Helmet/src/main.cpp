#include <Arduino.h>
#include <HardwareSerial.h>
#include <TinyGPS++.h>

TinyGPSPlus gps;
HardwareSerial GPS_Serial(2); 

void setup() {
  Serial.begin(115200);
  GPS_Serial.begin(9600, SERIAL_8N1, 16, 17); // Rx , TX
  Serial.println("Waiting for GPS signal.....");
}

void loop() {

  while(GPS_Serial.available() > 0){ // if the GPS is available then encode the data
    gps.encode(GPS_Serial.read());
  }

  if (gps.location.isUpdated()){
    Serial.print("Latitude: ");
    Serial.println(gps.location.lat(), 6);
    Serial.print("Longitude: ");
    Serial.println(gps.location.lng(), 6);
  }

  if (gps.speed.isUpdated()) {
    Serial.print("Speed (km/h) : ");
    Serial.print(gps.speed.kmph());
  }

  
}
