package com.demo.akka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.admin.AdminUtils;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.utils.ZkUtils;
import kafka.utils.ZKStringSerializer$;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;

import com.demo.FiniteStateMachine.FSMBase;
import com.demo.FiniteStateMachine.State;
import com.demo.kafka.SimpleConsumer;

@Component
@Scope("prototype")
public class FiniteStateMachineConsumerActor extends UntypedActor {
	
	@Value("${consumertopic}")
	private String topic;
	
	private static Logger logger = LoggerFactory.getLogger(FiniteStateMachineConsumerActor.class);
	
	@Autowired	
	private FSMBase fsmBase;
	private SimpleConsumer sc = null;
	private ConsumerConnector consumerConnector = null;

	@Override
	public void onReceive(Object msg) throws Exception {
		if(msg.toString().equals("ACTIVE")){
			System.out.println("inside active");
			sc = new SimpleConsumer("localhost:2181", "coregroup");
	        consumerConnector = sc.getConsumerConnector();
	        logger.info("Running.....");
	        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
	        topicCountMap.put(topic, new Integer(1));
	        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector
	                .createMessageStreams(topicCountMap);
	        KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);//remove args if error
	        ConsumerIterator<byte[], byte[]> it = stream.iterator();
	        while (it.hasNext()) {
	        	if(fsmBase.getState().equals(State.IDLE)){
	            	consumerConnector.shutdown();
		            break;
	            }
	            String newString = new String(it.next().message());
	            logger.info("newString :: {}",newString);
	            processMessage(newString);	
	            
	            
	        }							
		}									
	}
	
	
	public void processMessage(String newString){
		JSONObject jsonObj = this.isJSONValid(newString);
		if(jsonObj != null){
			System.out.println(newString);
			fsmBase.setState(State.IDLE);
		}
    	 
    	
	}
	
	
	public JSONObject isJSONValid(String test) {
		JSONObject jsonObj = null;
	    try {
	       jsonObj =  new JSONObject(test);
	       if(jsonObj.has("requestId")&& jsonObj.get("requestId").toString().equalsIgnoreCase("12345")){
	    	   return jsonObj;
	       }
	    } catch (JSONException ex) {	        
	       
	    }
	    return null;
	}

}
