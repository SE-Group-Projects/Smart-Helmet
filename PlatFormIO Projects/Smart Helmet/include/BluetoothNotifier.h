#ifndef BLUETOOTH_NOTIFIER_H
#define BLUETOOTH_NOTIFIER_H

#include <Arduino.h>

class BluetoothNotifier {
public:
    BluetoothNotifier(const char* phoneNumber, int simTxPin, int simRxPin);
    void setup();
    void sendCollisionAlert(double lat, double lon);

private:
    const char* _phoneNumber;
    int _simTxPin, _simRxPin;
};
#endif