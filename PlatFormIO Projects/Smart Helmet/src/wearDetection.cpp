#include <Arduino.h>
#include <Wire.h>
#include <Adafruit_VL53L0X.h>

Adafruit_VL53L0X lox = Adafruit_VL53L0X();

void setup(){
    Serial.begin(115200);

    // Initailize I2C..
    Wire.begin(21, 22);

    if (!lox.begin()){
        Serial.println("Failed to detect VL53L0X sensor!");
        while(1);
    }

    Serial.println("VL53L0X sensor Initialized.");
}

void loop(){
    VL53L0X_RangingMeasurementData_t measure;
    lox.rangingTest(&measure, false); // pass 'true t oget debubg output

    if (measure.RangeStatus != 4){
        Serial.print("Distance (mm): ");
        Serial.println(measure.RangeMilliMeter);

        // Check if helmet is worn (distance is less than 100 mm)
        if (measure.RangeMilliMeter < 100){
            Serial.println("Helmet is begin worn");
        }else {
            Serial.println("Helmet is not worn ");
        }
    }else {
        Serial.println("Out of range");
    }

    delay(1000);
}