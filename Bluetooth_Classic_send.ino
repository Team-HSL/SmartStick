#include "BluetoothSerial.h"
 
BluetoothSerial bt;
const char *bt_name = "bt-HSL"; // Bluetoothのデバイス名
 
void setup() {
  Serial.begin(115200);
  bt.begin(bt_name);
}

int i;

void loop() {
  bt.println(i);
  i++;
  delay(100);
}
