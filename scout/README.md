MQTT Scout Client
====

This is a MQTT client based on the Scout framework. 
The Scout client providing a graphical user interface to connect, 
subscribe to topic filters, receive messages. 
In a separate tab, the arduino "alarm" client can be armed and disarmed. 

* org.eclipse.paho.client Paho library plugin
* org.eclipsescout.mqttclient.client Scout client model for MQTT client
* org.eclipsescout.mqttclient.client.mobile mobile application specific components
* org.eclipsescout.mqttclient.client.shared application texts, icons, etc
* org.eclipsescout.mqttclient.client.target target platform definition
* org.eclipsescout.mqttclient.client.ui.rap rendering components for web/mobile application 
* org.eclipsescout.mqttclient.client.ui.swing rendering components for swing application 
* org.eclipsescout.mqttclient.client.ui.swt rendering components for eclipse rcp application 

The above plugin projects represent the complete MQTT client. 
Depending on the desired client (Swing, SWT, or RAP) you will find the necessary product files in the *.ui.{rap,swing,swt} projects.
