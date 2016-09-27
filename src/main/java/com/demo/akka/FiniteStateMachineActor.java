package com.demo.akka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.UntypedActor;

import com.demo.FiniteStateMachine.FSMBase;
import com.demo.FiniteStateMachine.State;
import com.demo.kafka.SpringBootKafkaProducer;

@Component
@Scope("prototype")
public class FiniteStateMachineActor extends UntypedActor {
	@Value("${topic}")
	private String topic;
	private static Logger logger = LoggerFactory.getLogger(FiniteStateMachineActor.class);
	
	@Autowired
    SpringBootKafkaProducer kafkaProducer;
	
	@Autowired
	FSMBase fsmBase;
	
	@Override
	public void onReceive(Object msg) throws Exception {
		kafkaProducer.send(msg.toString());	
	}

}
