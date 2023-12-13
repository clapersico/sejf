package org.sejf.core.components.dependencies;

import java.util.LinkedList;
import java.util.List;

public class ComponentCreationLog {	
	private List<String> log;
	
	public ComponentCreationLog() {
		this.log = new LinkedList<>();
	}
	
	public void register(String componentName) {
		log.add(componentName);
	}
}
