#include <Arduino.h>
#include <TinyGPSPlus.h>

#define FSR_PIN 34       // FSR analog pin
#define GATE_PIN 25      // MOSFET gate control
#define GPS_RX 16        // GPS TX → ESP RX
#define GPS_TX 17        // GPS RX → ESP TX

TinyGPSPlus gps;
HardwareSerial gpsSerial(1); // Use Serial1 for GPS

unsigned long lastPressureTime = 0;
bool systemOn = false;

// SIM800L pins (adjust according to wiring)
#define SIM_RX 26
#define SIM_TX 27
HardwareSerial simSerial(2);

// Function declaration with matching types
void sendSMS(double latitude, double longitude);

void setup() {
  Serial.begin(115200);

  pinMode(GATE_PIN, OUTPUT);
  digitalWrite(GATE_PIN, LOW);

  gpsSerial.begin(9600, SERIAL_8N1, GPS_RX, GPS_TX);
  simSerial.begin(9600, SERIAL_8N1, SIM_RX, SIM_TX);

  Serial.println("System Ready");
}

void loop() {
  int fsrValue = analogRead(FSR_PIN);

  // Detect helmet worn
  if (fsrValue > 1000) {
    lastPressureTime = millis();
    if (!systemOn) {
      systemOn = true;
      digitalWrite(GATE_PIN, HIGH);
      Serial.println("System ON");
    }
  }

  // Turn off if no pressure for 1 min
  if (systemOn && millis() - lastPressureTime > 30000) {
    systemOn = false;
    digitalWrite(GATE_PIN, LOW);
    Serial.println("System OFF");
  }

  // If system is on, process GPS and SIM
  if (systemOn) {
    while (gpsSerial.available()) {
      gps.encode(gpsSerial.read());
    }

    if (gps.location.isUpdated()) {
      double lat = gps.location.lat();
      double lng = gps.location.lng();

      Serial.printf("Lat: %.6f, Lng: %.6f\n", lat, lng);

      // Send location over SIM800L (every 10 sec)
      static unsigned long lastSend = 0;
      if (millis() - lastSend > 10000) {
        sendSMS(lat, lng);
        lastSend = millis();
      }
    }
  }
}

// Function definition with same signature as declaration
void sendSMS(double lat, double lng) {
  String message = "Helmet Location: https://maps.google.com/?q=" +
                   String(lat, 6) + "," + String(lng, 6);

  simSerial.println("AT+CMGF=1"); // Text mode
  delay(500); 
  simSerial.println("AT+CMGS=\"+94702016212\"");
  simSerial.print(message);
  delay(500);
  simSerial.write(26); // CTRL+Z to send
  Serial.println("SMS Sent: " + message);
}
