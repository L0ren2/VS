package ourservlet;

import java.io.IOException;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttReceiver {
	private MqttClient subscriber;
	private String topic;
	private String IP;
	private Endpoint endpoint;
	public MqttReceiver(String topic, String ipAddr, String Port, Endpoint endpoint) throws IOException, MqttException {
		this.topic = topic;
		this.IP = ipAddr + ":" + Port;
		this.endpoint = endpoint;
	}
	public void connect() throws IOException, MqttException {
		// connection for connecting with the topic
		String subID = UUID.randomUUID().toString();
		System.out.println("Connecting to Mqtt Topic...");
		subscriber = new MqttClient(IP, subID);
		subscriber.connect();
		System.out.println("Connected to Topic");
		subscriber.setCallback(new MqttCallback() {
			@Override
			public void connectionLost(Throwable arg0) {}
			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {}
			@Override
			public void messageArrived(String arg0, MqttMessage arg1) {}
		});
		subscriber.subscribe(topic, (topic, msg) -> {
			String message = new String(msg.getPayload());
			System.out.println(message);
			endpoint.sendMessage(message);
		});
		// let thread run to automatically poll for new messages
	}
	public void close() throws MqttException {
		if(subscriber.isConnected())
			this.subscriber.disconnect();
		this.subscriber.close();
	}
}



