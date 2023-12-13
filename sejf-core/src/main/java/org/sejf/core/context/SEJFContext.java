package org.sejf.core.context;

import java.util.Optional;

import org.sejf.core.components.ComponentManager;

public class SEJFContext {
	private ComponentManager cm;
	
	public SEJFContext(ComponentManager cm) {
		this.cm = cm;
	}
	
	public Optional<Object> getInstance(String componentName) {
		if(this.isInstancePresent(componentName)) {
			return Optional.of(this.cm.getAllInstantiatedComponent().get(componentName));
		}else
			return Optional.empty();
	}
	
	public boolean isInstancePresent(String componentName) {
		return this.cm.getAllInstantiatedComponent().containsKey(componentName);
	}
}
