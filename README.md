# SmartStickのリポジトリ
ROHM open hack challengeに提出する「SmartStick」に関わる全コード．

## 構成
+ ~~BLE_send.ino:   ESP32に書き込むコード．~~  <- 削除しました．
+ ESP32に書き込むコード:      Bluetooth_Classic_send.ino
+ Android　Studioプロジェクト: SmartStick_HSL    


## ESP32
Wi-fiもBluetoothも搭載した有能マイコン．開発環境はESP-IDF，ARduino IDE，Micro Pythonから選択できますが，Arduino IDEが良いでしょう．調べた時の参考資料の数が最も多いです．また，MicroPythonでは使える機能の制限が酷く，なんとBluetoothを使えません．これはだめだ．詳細を知りたい方は雑誌『インターフェース』のESP32の特集やってる一冊を読むと良いです．

Arduino-IDEを用いる際はボードの設定を行う必要があります．
- 参考：https://www.mgo-tec.com/arduino-core-esp32-install

macで開発を行う場合は，ポートを認識させるために下のドライバのインストールが必要です．以下のURLからMacのものをダウンロード，インストールしてください．
- https://jp.silabs.com/products/development-tools/software/usb-to-uart-bridge-vcp-drivers#mac

macOSがHigh Sierraになあなたは開発できません．残念．VirtualboxでWin10を導入して試しましたがダメでした．
Bootcampは試してません．誰かやってください．

Bluetooth Classic と Bluetooth Low Energy (BLE)は異なる規格です．ESP32では両方使えます．
利用する規格により実装が異なります．SmartStickではBluetooth Classicを使います．


## Androidアプリ
Android Studioで開発を行います．言語はJava．
アプリ起動前にAndroid端末のBluetoothをONにしておく必要があります．OFFのまま起動すると，落ちます．
ESP32とのペアリングは必須ではないはずです．
