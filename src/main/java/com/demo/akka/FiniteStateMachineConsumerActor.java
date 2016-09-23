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
	
	ZkClient zkClient = null;
    ZkUtils zkUtils = null;
    SimpleConsumer simpleConsumer = new SimpleConsumer("localhost:2181", "coregroup");

	@Override
	public void onReceive(Object msg) throws Exception {
		if(msg.toString().equals("ACTIVE")){
			System.out.println("inside active");
			zkClient = new ZkClient("localhost:2181", 10000, 10000, ZKStringSerializer$.MODULE$);
			Properties props = new Properties();
			zkUtils = new ZkUtils(zkClient, new ZkConnection("localhost:2181"), false);
			if(!AdminUtils.topicExists(zkUtils, topic)){
				System.out.println("topic created");
				AdminUtils.createTopic(zkUtils, topic, 10, 1, props, null);
				SimpleConsumer sc = new SimpleConsumer("localhost:2181", "coregroup");
		        ConsumerConnector consumerConnector = sc.getConsumerConnector();
		        logger.info("Running.....");
		        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		        topicCountMap.put(topic, new Integer(1));
		        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector
		                .createMessageStreams(topicCountMap);
		        KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);//remove args if error
		        ConsumerIterator<byte[], byte[]> it = stream.iterator();
		        while (it.hasNext()) {
		            String newString = new String(it.next().message());
		            logger.info("newString :: {}",newString);
		            processMessage(newString);	
		            consumerConnector.shutdown();
		            break;
		        }
			}				
		}	
		else{
			System.out.println("inside idle");
			String zookeeperHosts = "localhost:2181";		
			zkClient = new ZkClient("localhost:2181", 10000, 10000, ZKStringSerializer$.MODULE$);
			zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperHosts), false);
			if(AdminUtils.topicExists(zkUtils, topic)){
				AdminUtils.deleteTopic(zkUtils, topic);	
				System.out.println("topic deleted");
			}
		}								
	}
	
	
	public void processMessage(String newString){	
		//System.out.println("execute once");
    	System.out.println(newString);      
    	fsmBase.setState(State.IDLE);
}

}
