MQTT Arduino Components
====

PubSubClient: Arduino Client for MQTT
http://knolleary.net/arduino-client-for-mqtt/

mqttClient: arduino sketch related to the alarm tab in the Scout MQTT client

Subscribes topics: 
* /ece/scout/armed

Publishes to topics: 
* /ece/scout/light
* /ece/scout/alarm

Publishes Volts (0-5) measured from light sensor to topic /ece/scout/light and changes to the alarm status topic /ece/scout/alarm. 
Message payloads are of type string (char []).
