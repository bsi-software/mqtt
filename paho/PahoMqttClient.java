import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class PahoMqttClient {

    static    String broker       = "tcp://<your mqtt broker address>:1883"; // or use "tcp://iot.eclipse.org:1883"
    static    String clientId     = "Paho Client";
    static    String topic        = "eclipse/scout/arduino/";
    static    String content      = "RELAIS ON";
	static    boolean retained    = false;
		
    public static void main(String[] args) {
		if(args.length == 2) {
		    topic = args[0];
			content = args[1];
		}
		
		printConfiguration();
		
        try {
            MemoryPersistence  persistence = new MemoryPersistence();
            MqttClient         client      = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions opts        = new MqttConnectOptions();
            MqttMessage        message     = new MqttMessage(content.getBytes());
			
			message.setRetained(retained);
			
            client.connect(opts);             System.out.println("Connected to broker");			
            client.publish(topic, message);   System.out.println("Message '"+content+"' published to topic '"+topic+"'");
            client.disconnect();              System.out.println("Disconnected");
			
            System.exit(0);
        } 
		catch(MqttException me) {
            me.printStackTrace();
        }
    }
	
	private static void printConfiguration() {
        System.out.println("Setting broker to '"+broker+"'");
		System.out.println("Setting clientId to '"+clientId+"'");
		System.out.println("Setting topic to '"+topic+"'");
		System.out.println("Setting content to '"+content+"'");
		System.out.println("Setting retained to "+retained);
		System.out.println();
	}
}