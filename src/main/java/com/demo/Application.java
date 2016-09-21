package com.demo;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.demo.akka.AkkaInitializer;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
    	ApplicationContext context =  SpringApplication.run(Application.class, args);
    	ContextContainer.setContext(context);
        AkkaInitializer init = new AkkaInitializer(); 
    }

}
