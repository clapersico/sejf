package test.stub;

import org.sejf.core.components.singleton.*;

@Singleton
public class StubClassB {
	private StubClassA a;
	
	public StubClassB(StubClassA a) {
		this(a, null);
	}
	
	public StubClassB(StubClassA a, StubClassC c) {
		this.a = a;
	}
	
	public void doSomething() {
		System.out.println("B");
		a.doSomething();
		System.out.println("------------");
	}
}
