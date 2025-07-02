#include <Arduino.h>
#define  LED 2

void setup() {
  Serial.begin(115200);
  pinMode(2, OUTPUT);  // or try LED_BUILTIN
}

void loop() {
  Serial.println("Blink ON");
  digitalWrite(2, HIGH);
  delay(500);
  Serial.println("Blink OFF");
  digitalWrite(2, LOW);
  delay(500);
}
