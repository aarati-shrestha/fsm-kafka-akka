package com.demo.config;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.Extension;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import akka.actor.Props;

@Component
public class SpringExtension implements Extension {

    private ApplicationContext applicationContext;

    /**
     * Used to initialize the Spring application context for the extension.
     */
    public void initialize(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Create a Props for the specified actorBeanName using the
     * SpringActorProducer class.
     */
    public Props props(String actorBeanName) {
        return Props.create(SpringActorProducer.class,
            applicationContext, actorBeanName);
    }

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCritical() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte[] getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void encode(OutputStream out) throws IOException {
		// TODO Auto-generated method stub
		
	}
}

