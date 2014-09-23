/*
 Publishing in the callback 
 
  - connects to an MQTT server
  - subscribes to the topic "inTopic"
  - when a message is received, republishes it to "outTopic"
  
  This example shows how to publish messages within the
  callback function. The callback function header needs to
  be declared before the PubSubClient constructor and the 
  actual callback defined afterwards.
  This ensures the client reference in the callback function
  is valid.


  Temperatur-Sensor: 
    Flache Seite schaut zu dir: 
     Linker Fuss: 5 V
     mittlerer Fuss = Analog 0
     rechter Fuss: GND

  
*/

#include <SPI.h>
#include <Ethernet.h>
#include <PubSubClient.h>
#include <stdlib.h>

// Update these with values suitable for your network.
byte mac[]    = { 0xDE, 0xED, 0xBA, 0xFE, 0xFE, 0xED }; // mac address from ethernet shield sticker
byte server[] = { 192, 168, 43, 164 };                   // ip address of mqtt broker

float temperatureC = 0;
int tempReading = 0;
char tempC[6];
char message_buff[100];
char* buf = "";

// callback declaration header
void mqttCallback(char* topic, byte* payload, unsigned int length);

EthernetClient ethClient;
PubSubClient client(server, 1883, mqttCallback, ethClient);

void mqttCallback(char* topic, byte* payload, unsigned int length) {
  // In order to republish this payload, a copy must be made
  // as the orignal payload buffer will be overwritten whilst
  // constructing the PUBLISH packet.
  Serial.println("mqtt callback");
  Serial.print("topic='");
  Serial.print(topic);
  Serial.print("', message='");
  Serial.print(bytesToString(payload, length));
  Serial.println("'");
  
  // create character buffer (string) from byte buffer
  int i;
  for(i=0; i<length; i++) {
    message_buff[i] = payload[i];
  }
  message_buff[i] = '\0';
  
  String msgString = String(message_buff);
  
  
  Serial.print("old output");
  Serial.println(msgString);
  
  if (msgString.equals("off")) {
    digitalWrite(2, HIGH);
    
  } else if (msgString.equals("on")) {
    digitalWrite(2, LOW);
  }
  
}

String bytesToString(byte* payload, unsigned int length) {
  char buf[100];
  
  for(int i=0; i < length; i++) {
    buf[i] = payload[i];
  }
  
  buf[length] = '\0';
  
  return String(buf);
}

void setup()
{
  Serial.begin(9600);
  
  if(Ethernet.begin(mac) == 1) {
    Serial.println("dhcp connection successful");
    Serial.print("ip address of arduino board: ");
    Serial.println(Ethernet.localIP());
  }
  else {
    Serial.println("dhcp connection failed");
  }
  
  pinMode(2, OUTPUT); 
  digitalWrite(2, HIGH);
  
  if (client.connect("arduinoClient", "user2", "pw2")) {
    Serial.println("mqtt connect ok");
    client.subscribe("inTopic");
    Serial.println("mqtt subscribe topic");
  } else { 
    Serial.print("mqtt connect fail. check if ");
    Serial.print(server[0]);
    Serial.print(".");
    Serial.print(server[1]);
    Serial.print(".");
    Serial.print(server[2]);
    Serial.print(".");
    Serial.print(server[3]);
    Serial.print(" matches with the mqtt server address");
  }
}

void loop()
{
  client.loop();
  delay(1000);
  
  tempReading = analogRead(A0);
  
  float tempVoltage = tempReading * 5;
  tempVoltage /= 1024.0;
  temperatureC = (tempVoltage - 0.5) * 100 ;
  dtostrf(temperatureC, 6, 2, tempC);
  
  client.publish("Temp", tempC);
}
