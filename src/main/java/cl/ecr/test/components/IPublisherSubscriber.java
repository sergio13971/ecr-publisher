package cl.ecr.test.components;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface IPublisherSubscriber 
{
	void createClient(String uri, String idPublisher) throws MqttException;
	
	void setClientOptions();
	
	boolean connectionIsOk();
	
	void establishConnection() throws MqttException;
	
	void publishMessage(String destination, String msg) throws MqttException;
}
