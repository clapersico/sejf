package org.sejf.core;

import org.junit.jupiter.api.Test;
import org.sejf.core.context.SEJFContext;

import test.stub.StubClassB;

public class SimpleEnterpriseJavaFrameworkTest {
	
	@Test
	void loadContext() {
		SimpleEnterpriseJavaFramework.load();
		SEJFContext context = SimpleEnterpriseJavaFramework.load("test");
		
		((StubClassB) context.getInstance("test.stub.StubClassB").get()).doSomething();
	}
}
