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
		//actorRef.tell("start", null);
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		actorRef.tell("s2", null);
//		actorRef.tell("s3", null);
		
	}	
}


