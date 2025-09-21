#include <Arduino.h>
#include <TinyGPSPlus.h>
#include <HardwareSerial.h>

TinyGPSPlus gps;
HardwareSerial gpsSerial(1); // Use UART1

#define GPS_RX 16 // GPS TX → ESP32 RX
#define GPS_TX 17 // GPS RX ← ESP32 TX

void setup() {
  Serial.begin(115200);
  gpsSerial.begin(9600, SERIAL_8N1, GPS_RX, GPS_TX);
  Serial.println("NEO-6M GPS Location Test");
}

void loop() {
  while (gpsSerial.available()) {
    gps.encode(gpsSerial.read());
  }

  if (gps.location.isUpdated()) {
    Serial.print("Latitude: ");
    Serial.println(gps.location.lat(), 6);
    Serial.print("Longitude: ");
    Serial.println(gps.location.lng(), 6);
    Serial.print("Speed (km/h): ");
    Serial.println(gps.speed.kmph(), 2);
    Serial.print("Altitude (m): ");
    Serial.println(gps.altitude.meters());
    Serial.println("-----------------------");
  }
}
