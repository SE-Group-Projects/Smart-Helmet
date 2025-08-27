#include "BlynkManager.h"

BlynkManager::BlynkManager(const char* authToken, const char* ssid, const char* password)
    : _authToken(authToken), _ssid(ssid), _password(password) {}

void BlynkManager::setup(){
    Serial.print("Connecting to WIFI : ");
    Serial.println(_ssid);
    WiFi.begin(_ssid, _password);
    while(WiFi.status() != WL_CONNECTED){
        delay(500);
        Serial.print(".");
    }
    Serial.println("\nWiFi connected!");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());

    Blynk.config(_authToken);
    Serial.println("Blynk Manager initialized.");
}

void BlynkManager::run() {
    Blynk.run();
}

void BlynkManager::updateSystemStatus(bool isOn){
    if(isOn){
        Blynk.virtualWrite(VPIN_STATUS_LED, 255);
    }else{
        Blynk.virtualWrite(VPIN_STATUS_LED, 0);
    }
}

