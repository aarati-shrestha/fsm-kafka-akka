package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.service.FSMManager;

@RestController
public class FsmController {
	
	@Autowired	
	FSMManager fsmManager;
	
	@RequestMapping(value = "/sample", method = RequestMethod.POST)
	public void testFsm(@RequestBody String json){
		//System.out.println("hit"+ json);
		
		fsmManager.doSomething(json);
	}
	

}
