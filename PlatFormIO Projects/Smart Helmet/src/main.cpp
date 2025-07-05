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
  Serial.println("Blink ON");
  digitalWrite(2, HIGH);
  delay(500);
  Serial.println("Blink OFF");
  digitalWrite(2, LOW);
  delay(500);
}
