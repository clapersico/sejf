package test.stub;

import java.util.Date;

import org.sejf.core.components.singleton.*;

@Singleton
public class StubClassA {
	public StubClassA() {}
	
	public void doSomething() {
		System.out.println(new Date());
	}
}
