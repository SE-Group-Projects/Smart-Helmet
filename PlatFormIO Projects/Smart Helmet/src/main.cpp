#include <Arduino.h>
#include <Wire.h>
#include <Adafruit_VL53L0X.h>

#define SDA_PIN 21
#define SCL_PIN 22
#define XSHUT_PIN 25 // Used to power on sensor

Adafruit_VL53L0X lox = Adafruit_VL53L0X();

void setup() {
  Serial.begin(115200);
  delay(1000);  // Wait for serial monitor
  
  Serial.println("VL53L0X Wear Detection Starting...");

  // Initialize XSHUT
  pinMode(XSHUT_PIN, OUTPUT);
  digitalWrite(XSHUT_PIN, LOW);
  delay(10);  // Reset the sensor
  digitalWrite(XSHUT_PIN, HIGH);
  delay(10);  // Wait for sensor boot

  // Start I2C
  Wire.begin(SDA_PIN, SCL_PIN);

  if (!lox.begin()) {
    Serial.println("⚠️ Failed to initialize VL53L0X sensor. Check wiring and power.");
    while (1);
  }

  Serial.println("✅ VL53L0X sensor initialized.");
}

void loop() {
  VL53L0X_RangingMeasurementData_t measure;
  lox.rangingTest(&measure, false); // false = no debug output

  if (measure.RangeStatus != 4) {  // 4 means out of range
    Serial.print("👷 Distance: ");
    Serial.print(measure.RangeMilliMeter);
    Serial.print(" mm — ");

    if (measure.RangeMilliMeter < 100) {
      Serial.println("Helmet is being WORN ✅");
    } else {
      Serial.println("Helmet is NOT worn ❌");
    }
  } else {
    Serial.println("Out of range ❌");
  }

  delay(1000);
}
