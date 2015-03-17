## IoT Setup with open source components

![Arduino lamp off](/images/arduino_lamp_off_on.png)

*The final Setup. When completed, you will switch your desktop lamp on and off using your mobile phone.*

### The Final Setup

This project lets you control your AC desktop lamp and read sensor data using your mobile phone. The project was presented at the session [Build Your Open Source IoT Project from A to Z](https://www.doag.org/konferenz/konferenzplaner/konferenzplaner_details.php?locS=0&id=483801&vid=491425) at [JavaLand 2015](http://www.javaland.eu/javaland-2015/).

In the final setup the Raspberry Pi, the Arduino and the computer are all connected to the same wireless network. Both the Arduino and the computer will act as MQTT Clients that are connected to the MQTT broker running on the Raspberry Pi. The And the mobile phone to control the desktop lamp is using a HTML5 application that connects over HTTP to the web server running on computer.

### Components

#### Hardware
* Desktop lamp, relay module, and a modified power cable
* Mobile Phone
* Computer
* Arduino Uno with an Arduino Wifi Shield
* Breadboard, some LEDs, a Light-dependent Resistor (LDR) a normal resitor, cables
* Raspberry Pi (optional, used to run the MQTT broker)
* Wireless network

#### Software
* **Eclipse Paho** for the MQTT client on the computer
* **Eclipse Mosquitto** as the MQTT broker
* **Eclipse Scout** for the tooling and runtime to build the user interface (this includes the Eclipse IDE)
* **Arduino Software (IDE)** to program the Arduino using the computer

