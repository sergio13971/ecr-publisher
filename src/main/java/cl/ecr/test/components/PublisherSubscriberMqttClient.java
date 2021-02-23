package cl.ecr.test.components;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PublisherSubscriberMqttClient implements IPublisherSubscriber 
{
	private IMqttClient publisher;
	private MqttConnectOptions options;
	
	@Override
	public void createClient(String uri, String idPublisher) throws MqttException
	{
		publisher = new MqttClient(uri, idPublisher);
	}

	@Override
	public void setClientOptions() 
	{
    	options = new MqttConnectOptions();
    	options.setAutomaticReconnect(true);
    	options.setCleanSession(true);
    	options.setConnectionTimeout(10);
	}

	@Override
	public boolean connectionIsOk() 
	{
		return publisher.isConnected();
	}

	@Override
	public void establishConnection() throws MqttException
	{
		publisher.connect(options);
	}

	@Override
	public void publishMessage(String destination, String message)
			throws MqttException 
	{
		publisher.subscribe(destination, new IMqttMessageListener() {
			
			@Override
			public void messageArrived(String arg0, MqttMessage mqttMessage) throws Exception 
			{
				System.out.println("[param1] => " + arg0 + " [mqttMsg] => " + new String(mqttMessage.getPayload()));
			}
		});

        MqttMessage msg = new MqttMessage(message.getBytes());
        msg.setQos(2);
        msg.setRetained(true);
        publisher.publish(destination, msg);
	}
}
