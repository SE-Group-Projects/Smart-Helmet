#include <Arduino.h>
#define  LED 2
// put function declarations here:
int myFunction(int, int);

void setup() {
  Serial.begin(115200);
  pinMode(2, OUTPUT);
}

void loop() {
  digitalWrite(2, HIGH);
  delay(500);
  digitalWrite(2, LOW);
  delay(500);
}


// put function definitions here:
int myFunction(int x, int y) {
  return x + y;
}