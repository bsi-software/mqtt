## IoT Setup with open source components

![Arduino lamp off](/images/arduino_lamp_off_on.png)

*The final Setup. When completed, you will switch your desktop lamp on and off using your mobile phone.*

### The Final Setup

This project lets you control your AC desktop lamp and read sensor data using your mobile phone. The project was presented at the session [Build Your Open Source IoT Project from A to Z](https://www.doag.org/konferenz/konferenzplaner/konferenzplaner_details.php?locS=0&id=483801&vid=491425) at [JavaLand 2015](http://www.javaland.eu/javaland-2015/) using [these slides](https://wiki.eclipse.org/images/3/35/150318_open_source_iot_printing.pdf).

In the final setup the Raspberry Pi, the Arduino and the computer are all connected to the same wireless network. Both the Arduino and the computer will act as MQTT Clients that are connected to the MQTT broker running on the Raspberry Pi. The And the mobile phone to control the desktop lamp is using a HTML5 application that connects over HTTP to the web server running on computer.

For the communication between the Arduino, the broker and the computer the [MQTT](http://mqtt.org/) protocol is used. This protocol was designed as an extremely lightweight publish/subscribe messaging transport and is working well for internet of things (IoT) applications. All components are communicating over the same wireless network.

To simplify the setup and/or save costs and time you have the following options:
* Remove Raspberry and Mosquitto from the project. Use a public MQTT broker instead: tcp://iot.eclipse.org:1883
* Remove the nano USB wireless adapter and replace the Arduino WiFi shield with an Ethernet shield. Use Ethernet cables instead of wireless connections

### Hardware
* Desktop lamp, relay module, and a modified power cable
* Mobile Phone
* Computer
* Arduino Uno with an Arduino Wifi Shield
* Breadboard, some LEDs, a Light-dependent Resistor (LDR) a normal resitor, cables
* Raspberry Pi with a nano USB wireless adapter
* Wireless network

### Software
* [Eclipse Paho](https://eclipse.org/paho/) for the MQTT client on the computer
* [Eclipse Mosquitto](https://www.eclipse.org/mosquitto/) as the MQTT broker
* [Eclipse Scout](https://eclipse.org/scout/) for the tooling and runtime to build the user interface
* [Arduino Software (IDE)](http://arduino.cc/en/main/software) to program the Arduino using the computer
* [Arduino Client for MQTT](http://knolleary.net/arduino-client-for-mqtt/)

