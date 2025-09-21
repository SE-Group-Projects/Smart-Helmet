#include <Arduino.h>
#define BATTERY_PIN 34   // Analog input pin connected to voltage divider

// Battery parameters (adjust for your battery type)
const float MAX_BATTERY_VOLTAGE = 4.2;   // Full charge (Li-Ion/Li-Po)
const float MIN_BATTERY_VOLTAGE = 3.0;   // Cutoff voltage
const float R1 = 100000.0;  // Resistor 1 of divider (100kΩ)
const float R2 = 100000.0;  // Resistor 2 of divider (100kΩ)

float readBatteryVoltage() {
    int raw = analogRead(BATTERY_PIN);
    float voltage = ((float)raw / 4095.0) * 3.3; // ADC → ESP32 (12-bit, 3.3V ref)
    
    // Adjust for voltage divider
    voltage = voltage * (R1 + R2) / R2;
    return voltage;
}

int getBatteryPercentage() {
    float voltage = readBatteryVoltage();

    // Map voltage to percentage
    if (voltage > MAX_BATTERY_VOLTAGE) voltage = MAX_BATTERY_VOLTAGE;
    if (voltage < MIN_BATTERY_VOLTAGE) voltage = MIN_BATTERY_VOLTAGE;

    int percent = (int)(((voltage - MIN_BATTERY_VOLTAGE) / 
                        (MAX_BATTERY_VOLTAGE - MIN_BATTERY_VOLTAGE)) * 100);
    return percent;
}

void setup() {
    Serial.begin(115200);
    analogReadResolution(12); // ESP32 uses 12-bit ADC
}

void loop() {
    float voltage = readBatteryVoltage();
    int percent = getBatteryPercentage();

    Serial.print("Battery Voltage: ");
    Serial.print(voltage, 2);
    Serial.print(" V | Battery: ");
    Serial.print(percent);
    Serial.println("%");

    delay(2000); // update every 2s
}
