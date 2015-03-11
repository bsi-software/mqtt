#include <SPI.h>
#include <WiFi.h>
#include <PubSubClient.h>

// Mqtt setup
byte  MQTT_SERVER[]      = {<your mqtt broker ip>}; // use {198, 41, 30, 241} for iot.eclipse.org
char* MQTT_CLIENT_ID     = "MziArduino";
char* MQTT_TOPIC_COMMAND = "eclipse/scout/arduino/";
char* MQTT_TOPIC_STATUS  = "eclipse/scout/arduino/status";
char* MQTT_TOPIC_LDR     = "eclipse/scout/arduino/ldr";
char* MQTT_RELAIS_ON     = "RELAIS ON";
char* MQTT_RELAIS_OFF    = "RELAIS OFF";
char* MQTT_LDR_GET       = "LDR GET";

#define MQTT_PORT 1883
#define MQTT_MAX_PAYLOAD_SIZE 100

WiFiClient   wlanClient;
void         mqttCallback(char* topic, byte* payload, unsigned int length);
PubSubClient mqttClient(MQTT_SERVER, MQTT_PORT, mqttCallback, wlanClient);

// Wlan setup
char* SSID_NAME = "<your wlan name>";
char* PASS      = "<your wlan password>";

// Electronics setup
// pins 4,7,10-13 are used by wlan shield
#define PIN_MQTT_READY_GREEN 9
#define PIN_WLAN_READY_GREEN 8
#define PIN_WAITING_YELLOW 6
#define PIN_DISCONNECTED_RED 5

#define PIN_RELAIS 2
#define RELAIS_ON HIGH
#define RELAIS_OFF LOW

#define PIN_LDR A0

int LOG_DEBUG =   10;
int LOG_WARN  =   20;
int LOG_ERROR =   30;
int LOG_NONE  = 1000;
int log_level = LOG_DEBUG;

void setup() {
  initializeSerial();
  initializeElectronics();
  initializeWlan();
  connectToWlanAndMqtt();
}

void loop() {
  mqttClient.loop();
  reconnectIfNecessary();
  delay(100);
}

void mqttCallback(char* topic, byte* payload, unsigned int length) {
  // we expect to get a char sequence (this is an assumption!!!)
  char* message = bytesToChars(payload, length);
  
  if(strcmp(message, MQTT_RELAIS_ON) == 0) {
    digitalWrite(PIN_RELAIS, RELAIS_ON);
  }
  
  if(strcmp(message, MQTT_RELAIS_OFF) == 0) {
    digitalWrite(PIN_RELAIS, RELAIS_OFF);
  }
  
  if(strcmp(message, MQTT_LDR_GET) == 0) {
    int value = analogRead(PIN_LDR);
    char *buf = "1234";
    mqttClient.publish(MQTT_TOPIC_LDR, intToChars(value, buf));
  }
}

void reconnectIfNecessary() {
  // check if we are still ok
  if(mqttClient.connected()) {
    return;
  }
  
  if(log_level <= LOG_WARN) {
    Serial.println("WARNING: Connection lost. Retrying ...");
  }
  
  if(!wlanIsConnected()) {
    showLedStatus(PIN_DISCONNECTED_RED);    
  }
  else {
    showLedStatus(PIN_WLAN_READY_GREEN);    
  }
  
  connectToWlanAndMqtt();
}

void connectToWlanAndMqtt() {
  // check if we have to do something
  if(mqttClient.connected()) {
    return;
  }
  
  // check if we have to reconnect to the wlan network
  if(!wlanIsConnected()) {
    // check networks and connect to right wlan
    if(findRightNetwork(SSID_NAME) == 1) {
      connectToWiFi(SSID_NAME, PASS);
    }
  }
  
  // connect to mqtt server and subscribe topics
  if(wlanIsConnected()) {
    connectAndSubscribe(MQTT_CLIENT_ID);
  }
}

int connectAndSubscribe(char* clientId) {
  if(log_level <= LOG_DEBUG) {
    Serial.print("Connecting to Mqtt server ");
    printIpAddress(MQTT_SERVER);
    Serial.println(" ...");
  }
  
  if(mqttClient.connect(clientId)) {
    if(log_level <= LOG_DEBUG) {
      Serial.print("- Connected to Mqtt server");
    }
    
    mqttClient.subscribe(MQTT_TOPIC_COMMAND);
    mqttClient.publish(MQTT_TOPIC_STATUS, "ready for commands");
    
    if(log_level <= LOG_DEBUG) {
      Serial.print("- Subscribed topic '");
      Serial.print(MQTT_TOPIC_COMMAND);
      Serial.println("'");
      
      Serial.print("- Published status to '");
      Serial.print(MQTT_TOPIC_STATUS);
      Serial.println("'");
      
      Serial.print("- '");
      Serial.print(clientId);
      Serial.println("' is ready to receive commands");
    }
    
    showLedStatus(PIN_MQTT_READY_GREEN);    
    if(log_level <= LOG_DEBUG) {
      Serial.println("Connection to Mqtt server successful");
    }
  }
  else {
    if(log_level <= LOG_ERROR) {
      Serial.println("ERROR: Connection failed");
      Serial.print("- Check if your Mqtt server ");
      printIpAddress(MQTT_SERVER);
      Serial.println(" is alive");
    }
  }
}

int connectToWiFi(char* ssid, const char *passphrase) {
  showLedStatus(PIN_WAITING_YELLOW);
  
  if(log_level <= LOG_DEBUG) {
    Serial.print("Connecting to WiFi '");
    Serial.print(ssid);
    Serial.println("' ...");
  }
  
  int wifiStatus = WiFi.begin(ssid, passphrase);
 
  while(wifiStatus != WL_CONNECTED) {
    printWifiStatus(wifiStatus);
    delay(1000);
    wifiStatus = WiFi.status();
  }
  
  showLedStatus(PIN_WLAN_READY_GREEN);
  printWifiStatus(wifiStatus);
  
  return wifiStatus;
}

int wlanIsConnected() {
  if(WiFi.status() == WL_CONNECTED) {
    return 1;
  }
  else {
    return 0;
  }
}

int findRightNetwork(char* ssid) {
  showLedStatus(PIN_DISCONNECTED_RED);
  
  if(log_level = LOG_DEBUG) {
    Serial.print("Scan Networks ..");
  }
  
  byte numSsid = WiFi.scanNetworks();
  int found = 0;
  
  if(log_level = LOG_DEBUG) {
    Serial.println(".");
  }
  
  if(numSsid > 0) {
    for (int thisNet = 0; thisNet < numSsid; thisNet++) {
      if(log_level = LOG_DEBUG) {
        Serial.print("- SSID[");
        Serial.print(thisNet);
        Serial.print("] ");
        Serial.print(WiFi.SSID(thisNet));
        Serial.print(" (Strength: ");
        Serial.print(WiFi.RSSI(thisNet));
        Serial.print(" dBm, Encryption: ");
        Serial.print(WiFi.encryptionType(thisNet));
        Serial.println(")");
      }
      
      if(strcmp(WiFi.SSID(thisNet), ssid) == 0) {
        found = 1;
      }
    }
    
    if(found) {
       showLedStatus(PIN_WAITING_YELLOW);
       
       if(log_level <= LOG_DEBUG) {
        Serial.print("Success, Wifi network '");
        Serial.print(ssid);
        Serial.println("' is available");
      }
    }
  }
  else if (log_level <= LOG_ERROR){
    Serial.println("ERROR: No Wifi networks detected. ");
  }
  
  if(!found && log_level <= LOG_ERROR) {
    Serial.print("ERROR: Wifi network '");
    Serial.print(ssid);
    Serial.println("' not available. Verify setup and continue!");
  }
  
  return found;
}

void initializeSerial() {
  // initialize serial and wait for the port to open:
  Serial.begin(9600);
  while(!Serial);
}

void initializeElectronics() {
  // status led
  pinMode(PIN_MQTT_READY_GREEN, OUTPUT);     
  pinMode(PIN_WLAN_READY_GREEN, OUTPUT);     
  pinMode(PIN_WAITING_YELLOW, OUTPUT);     
  pinMode(PIN_DISCONNECTED_RED, OUTPUT);
  
  // sets all led pins to low
  showLedStatus(-1); 
  
  // relais led
  pinMode(PIN_RELAIS, OUTPUT);
  digitalWrite(PIN_RELAIS, RELAIS_OFF);
}

void initializeWlan() {
  // print mac address of this arduino
  if(log_level <= LOG_DEBUG) {
    Serial.println("Initializing WLan...");
    printMacAddress();
  }
}

void printWifiStatus(int wifiStatus) {
  switch(wifiStatus) {
  case WL_NO_SHIELD:
    Serial.println("ERROR: Arduino not connected to WiFi shield, halting ...");
    while(true);
    break;
  case WL_CONNECTED:
    if(log_level <= LOG_DEBUG) {
      Serial.println("Connection successful");
      
      // print the SSID of the network you're attached to:
      Serial.print("- SSID: ");
      Serial.println(WiFi.SSID());
    
      // print the received signal strength:
      Serial.print("- Strength (RSSI): ");
      Serial.print(WiFi.RSSI(), LOG_DEBUG);
      Serial.println(" dBm");
    
      // print your WiFi shield's IP address:
      Serial.print("- IP Address: ");
      Serial.println(WiFi.localIP());
    }
    break;
  case WL_CONNECT_FAILED:
    showLedStatus(PIN_DISCONNECTED_RED);
    if(log_level <= LOG_WARN) {
      Serial.println("Connection failed ...");
    }
    break;  
  default:
    showLedStatus(PIN_DISCONNECTED_RED);
    if(log_level <= LOG_WARN) {
      Serial.print("Not connected, status=");
      Serial.print(wifiStatus);
      Serial.println(" waiting ...");
    }
    break;  
  }
}

void showLedStatus(int ledStatus) {
  digitalWrite(PIN_MQTT_READY_GREEN, LOW);
  digitalWrite(PIN_WLAN_READY_GREEN, LOW);
  digitalWrite(PIN_WAITING_YELLOW, LOW);
  digitalWrite(PIN_DISCONNECTED_RED, LOW);
  
  int led_brightness = 128;
  
  switch(ledStatus) {
    case PIN_MQTT_READY_GREEN:
      digitalWrite(PIN_MQTT_READY_GREEN, led_brightness);
      digitalWrite(PIN_WLAN_READY_GREEN, led_brightness);
      break;
    case PIN_WLAN_READY_GREEN:
      digitalWrite(PIN_WLAN_READY_GREEN, led_brightness);
      break;
    case PIN_WAITING_YELLOW:
      digitalWrite(PIN_WAITING_YELLOW, led_brightness);
      break;
    case PIN_DISCONNECTED_RED:
      digitalWrite(PIN_DISCONNECTED_RED, led_brightness);
      break;
  }
}

void printIpAddress(byte* address) {
  Serial.print(address[0]);
  Serial.print(".");
  Serial.print(address[1]);
  Serial.print(".");
  Serial.print(address[2]);
  Serial.print(".");
  Serial.print(address[3]);
}

void printMacAddress() {
  byte mac[6];                     

  // print your MAC address:
  WiFi.macAddress(mac);
  Serial.print("- Arduino MAC address: ");
  Serial.print(mac[5],HEX);
  Serial.print(":");
  Serial.print(mac[4],HEX);
  Serial.print(":");
  Serial.print(mac[3],HEX);
  Serial.print(":");
  Serial.print(mac[2],HEX);
  Serial.print(":");
  Serial.print(mac[1],HEX);
  Serial.print(":");
  Serial.println(mac[0],HEX);
}

// http://stackoverflow.com/questions/9655202/how-to-convert-integer-to-string-in-c
char* intToChars(int i, char b[]){
  char const digit[] = "0123456789";
  char* p = b;
  int shifter = i;
  
  do { // move to where representation ends
    ++p;
    shifter = shifter/10;
  } while(shifter);
  *p = '\0';
  
  do { // move back, inserting digits as u go
    *--p = digit[i%10];
    i = i/10;
  } while(i);
  
  return b;
}

char* bytesToChars(byte* payload, unsigned int length) {
  char buf[MQTT_MAX_PAYLOAD_SIZE];
  int i = 0;

  for(; i < length && i < MQTT_MAX_PAYLOAD_SIZE; i++) {
    buf[i] = payload[i];
  }
  
  buf[i] = '\0';
  
  return buf;
}
