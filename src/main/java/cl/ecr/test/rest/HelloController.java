package cl.ecr.test.rest;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.ecr.test.components.IPublisherSubscriber;

@RestController
@RequestMapping("/ecr-test")
public class HelloController 
{
	private final String uriMqtt = "tcp://test.mosquitto.org:1883";
	
	@Value("${client.class}")
	private String clientClass;
	
	@Value("${server.list}")
	private String serverList; 
	
	@Value("${server.type}")
	private String serverType;
	
	@Value("${server.commands}")
	private String serverCommands;	
	
	private List<String> servers;

	private Map<String, List<String>> commandsByServer;
	
	@PostConstruct
	private void init()
	{
		System.out.println("[InitController]");
		servers = Arrays.asList(serverList.split("[;]"));
		
		commandsByServer = new HashMap<String, List<String>>();
		for (String commands: serverCommands.split("[;]"))
		{
			String[] cmds = commands.split("[@]");
			commandsByServer.put(cmds[0], Arrays.asList(cmds[1].split("[,]")));
		}
	}

    @RequestMapping(value = "/getallowedcommands", method = RequestMethod.POST, produces = {"application/json"})
    public List<String> getAllowedCommands() 
    {
        return commandsByServer.get(serverType);
    }    
    
    /**
     * Method to publish a command. 
     * @param sender Sender Id
     * @param destination Receiver Id
     * @param command Command
     * @return
     * @throws MqttException 
     * @throws ClassNotFoundException 
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    @RequestMapping(value = "/sendcommand", method = RequestMethod.POST)
    public Boolean sendCommand(@RequestParam String sender,
    						   @RequestParam String destination,
    						   @RequestParam String command) throws MqttException, 
    						   										InstantiationException, 
    						   										IllegalAccessException, 
    						   										IllegalArgumentException, 
    						   										InvocationTargetException, 
    						   										NoSuchMethodException, 
    						   										SecurityException, 
    						   										ClassNotFoundException
    {
    	Boolean result = Boolean.FALSE;
    	
    	IPublisherSubscriber publisher = (IPublisherSubscriber) Class.forName(clientClass).getConstructor(new Class[]{}).newInstance();
    	
    	publisher.createClient(uriMqtt, sender);
    	publisher.setClientOptions();
    	publisher.establishConnection();
    	
    	if (publisher.connectionIsOk())
    	{
    		publisher.publishMessage(destination, sender + ";" + command);
    		result = Boolean.TRUE;
    	}

    	return result;
    }
}
