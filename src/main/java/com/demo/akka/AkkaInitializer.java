package com.demo.akka;

import akka.actor.ActorRef;

import com.demo.ContextContainer;
import com.demo.config.SpringExtension;

public class AkkaInitializer {
	
	private ActorRef actorRef ;
	private ActorRef fsmConsumerActor;
	public AkkaInitializer() {
		SpringExtension ext = ContextContainer.getContext().getBean(SpringExtension.class);
		ext.initialize(ContextContainer.getContext());	
		
		this.actorRef  = AkkaFactory.getActorSystem()
				.actorOf(ext.props("finiteStateMachineActor"),"fsmactor");	
		this.fsmConsumerActor = AkkaFactory.getActorSystem()
				.actorOf(ext.props("finiteStateMachineConsumerActor"),"fsmConsumeractor");

		
	}	
}


