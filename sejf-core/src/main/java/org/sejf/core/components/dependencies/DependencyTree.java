package org.sejf.core.components.dependencies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.sejf.core.components.Component;

public class DependencyTree {
	private Map<String, List<String>> dependencies;
	
	public DependencyTree() {
		this.dependencies = new HashMap<>();
	}

	public void add(Component component) {
		List<String> depList = new ArrayList<>();
		
		for(String dependency : component.getDependencies())
			depList.add(dependency);
		
		this.dependencies.put(component.getComponentName(), depList);
	}
	
	public Optional<List<String>> get(String key){
		return this.dependencies.containsKey(key) ? Optional.of(this.dependencies.get(key)) : Optional.empty();
	}
	
	public Set<String> getAllComponents(){
		return this.dependencies.keySet();
	}
	
	public void delete(String componentName) {
		this.dependencies.remove(componentName);
	}
	
	public int size() {
		return this.dependencies.size();
	}
}