#include <Wire.h>
#include <Adafruit_BMP280.h>

Adafruit_BMP280 bmp; // I2C interface

void setup() {
  Serial.begin(115200);
  if (!bmp.begin(0x76)) { // Try 0x76 or 0x77 depending on your module
    Serial.println("BMP280 not found. Check wiring or address!");
    while (1);
  }
}

void loop() {
  Serial.print("Temperature = ");
  Serial.print(bmp.readTemperature());
  Serial.println(" °C");

  Serial.print("Pressure = ");
  Serial.print(bmp.readPressure() / 100.0); // Convert to hPa
  Serial.println(" hPa");

  delay(1000);
}
