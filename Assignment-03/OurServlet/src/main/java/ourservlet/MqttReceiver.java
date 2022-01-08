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
	private volatile Websocket websock;
	public MqttReceiver(String topic, String ipAddr, String Port, Websocket websock) throws IOException, MqttException {
		this.topic = topic;
		this.IP = ipAddr + ":" + Port;
		this.websock = websock;
	}
	public void connect() throws IOException, MqttException {
		// connection for connecting with the topic
		String subID = UUID.randomUUID().toString();
		System.out.println("UUID: " + subID);
		System.out.println("Connecting...");
		subscriber = new MqttClient(IP, subID);
		subscriber.connect();
		System.out.println("Connected");
		subscriber.setCallback(new MqttCallback() {
			@Override
			public void connectionLost(Throwable arg0) {}
			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {}
			@Override
			public void messageArrived(String arg0, MqttMessage arg1) {}
		});
		System.out.println("Callback set");
		subscriber.subscribe(topic, (topic, msg) -> {
			System.out.println("subscribed to topic");
			String message = new String(msg.getPayload());
			System.out.println(message);
			websock.send(message);
		});
		while(true) { } // let thread run to automatically poll for new messages
	}
	public void close() throws MqttException {
		if(subscriber.isConnected())
			this.subscriber.disconnect();
		this.subscriber.close();
	}
}



