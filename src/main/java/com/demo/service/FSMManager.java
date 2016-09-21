package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import akka.actor.ActorSelection;

import com.demo.FiniteStateMachine.FSMBase;
import com.demo.FiniteStateMachine.State;
import com.demo.akka.AkkaFactory;

@Component
public class FSMManager {
	
	@Autowired
	FSMBase fsmBase;
	
	ActorSelection fsmAkka = AkkaFactory.getActorSystem()
            .actorSelection("akka://AKKASystem/user/fsmactor");
	ActorSelection fsmConsumerAkka = AkkaFactory.getActorSystem()
            .actorSelection("akka://AKKASystem/user/fsmConsumeractor");
	public void doSomething(String data){
		if(fsmBase.getState().equals(State.IDLE)){
			fsmBase.setState(State.ACTIVE);
		}
		fsmAkka.tell(data, null);
		fsmConsumerAkka.tell(fsmBase.getState(), null);
		
		
		
		
		
		
		
		
	}

}
