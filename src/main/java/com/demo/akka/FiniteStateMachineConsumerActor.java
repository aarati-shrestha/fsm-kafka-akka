package com.demo.akka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Override
	public void onReceive(Object msg) throws Exception {
		if(msg.toString().equals("ACTIVE")){
			logger.info("GREETED");
	        logger.info("Consumer starting");
	        SimpleConsumer sc = new SimpleConsumer("localhost:2181", "coregroup");
	        ConsumerConnector consumerConnector = sc.getConsumerConnector();
	        logger.info("Running.....");
	        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
	        topicCountMap.put(topic, new Integer(1));
	        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector
	                .createMessageStreams(topicCountMap);
	        KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);//remove args if error
	        ConsumerIterator<byte[], byte[]> it = stream.iterator();
	        System.out.println("does this execute");
	        while (it.hasNext()) {
	            String newString = new String(it.next().message());
	            logger.info("newString :: {}",newString);
	            System.out.println("state"+ fsmBase.getState());
	            if(fsmBase.getState().toString().equals("ACTIVE")){
		            try {	            	
		            	processMessage(newString);	            		            		            	
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				else{
            		consumerConnector.shutdown();
            		break;
            	}
	            
	            
	        }
		}
		
		
	}
	
	
	public void processMessage(String newString){	
		System.out.println("execute once");
    	System.out.println(newString);      
    	fsmBase.setState(State.IDLE);
}

}
