package com.demo;

import org.springframework.context.ApplicationContext;

public class ContextContainer {
	private static ApplicationContext context;

	public static ApplicationContext getContext() {
		return context;
	}

	public static void setContext(ApplicationContext context) {
		ContextContainer.context = context;
	}
}
