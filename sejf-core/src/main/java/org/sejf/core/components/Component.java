package org.sejf.core.components;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Component {
	private boolean hasEmptyConstructor;
	
	@Getter
	private String componentName;
	
	@Getter
	private List<String> dependencies;
	
	public Component(String componentName) {		
		this.componentName = componentName;
		
		this.dependencies = new ArrayList<>();
	}
	
	public boolean addDependency(String dependency) {
		if(this.dependencies.contains(dependency)) return false;
		
		this.dependencies.add(dependency);
		
		return true;
	}
	
	public boolean hasEmptyConstructor() {
		return this.hasEmptyConstructor;
	}
	
	public void hasEmptyConstructor(boolean value) {
		this.hasEmptyConstructor = value;
	}
}
