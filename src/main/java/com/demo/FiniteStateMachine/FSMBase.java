package com.demo.FiniteStateMachine;

import org.springframework.stereotype.Component;






@Component
public class FSMBase  {

	private State state = State.IDLE;
	
	public State getState() {
	    return state;
	}
	public void setState(State s) {
	    if (state != s) {
	      state = s;
	    }
	 }
	
	
}
