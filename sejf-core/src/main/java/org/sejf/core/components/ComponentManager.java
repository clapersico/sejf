package org.sejf.core.components;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.sejf.core.components.dependencies.ComponentCreationLog;
import org.sejf.core.components.dependencies.DependencyTree;
import org.sejf.core.components.exceptions.NotInstantiableComponentException;

public class ComponentManager {
	private Map<String, Object> instantiatedComponent;
	private List<String> componentToInstantiate;
	private int sizeOfLastDependencyTree;
	private ComponentCreationLog componentCreationLog;
	
	public ComponentManager() {
		this.instantiatedComponent = new HashMap<String, Object>();
		this.componentToInstantiate = new ArrayList<>();
		this.sizeOfLastDependencyTree = 0;
		this.componentCreationLog = new ComponentCreationLog();
	}
	
	public boolean instantiateComponent(DependencyTree dependencyTree) {
		if(this.componentToInstantiate.size() == 0)
			this.componentToInstantiate = dependencyTree.getAllComponents().stream().toList();
		
		Set<String> components = new HashSet<>(dependencyTree.getAllComponents());
		
		for(String component : components) {
			Optional<List<String>> dependencyOptional = dependencyTree.get(component);
			
			if(instantiateComponent(component, dependencyOptional))
				dependencyTree.delete(component);
		}
		
		if(dependencyTree.size() > 0)
			if(dependencyTree.size() != this.sizeOfLastDependencyTree) {
				this.sizeOfLastDependencyTree = dependencyTree.size();
				instantiateComponent(dependencyTree);
			}else
				throw new NotInstantiableComponentException(dependencyTree);
		
		return true;
	}
	
	public Map<String, Object> getAllInstantiatedComponent(){
		return this.instantiatedComponent;
	}

	private boolean instantiateComponent(String component, Optional<List<String>> dependencyOptional) {
		List<String> dependencyList = new ArrayList<>();
		
		if(dependencyOptional.isPresent())
			dependencyList = dependencyOptional.get();
		
		try {
			Optional<Constructor<?>> constructorOptional = null;
			
			// Empty constructors
			if(dependencyList.size() == 1 && dependencyList.get(0).equals(""))
				constructorOptional = this.getConstructor(Class.forName(component));
			else if (dependencyList.size() == 1) // Dependency injection constructors
				constructorOptional = this.getConstructor(Class.forName(component), dependencyList.get(0));
			else
				constructorOptional = this.getConstructor(Class.forName(component), dependencyList);
			
			// Instantiate object
			if(constructorOptional != null && constructorOptional.isPresent()) {
				Constructor<?> constructor = constructorOptional.get();
				
				if(constructor.getParameterCount() == 0)
					this.instantiatedComponent.put(component, constructor.newInstance());
				else {
					List<Object> dependency = new ArrayList<Object>();
					
					for(Parameter p : constructor.getParameters()) {
						if(this.instantiatedComponent.containsKey(p.getType().getCanonicalName())){
							dependency.add(this.instantiatedComponent.get(p.getType().getCanonicalName()));
						}else
							return false;
					}
					
					this.instantiatedComponent.put(component, constructor.newInstance(dependency.toArray()));
					this.componentCreationLog.register(component);
				}
			}else 
				return false;
		}catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	private Optional<Constructor<?>> getConstructor(Class<?> clazz, List<String> params) {
		return this.getConstructor(clazz, Arrays.toString(params.toArray()));
	}
	
	private Optional<Constructor<?>> getConstructor(Class<?> clazz, String... params) {
		List<Constructor<?>> constructors = new ArrayList<>();
		
		for(Constructor<?> c : clazz.getConstructors()){	
			if(this.checkParametersMatch(c.getParameters()))
				constructors.add(c);
		}
		
		if(constructors.size() > 0) {
			if(constructors.size() > 1) {
				int maxNumberOfParameters = constructors.get(0).getParameterCount();
				int indexOfDesignatedConstructor = 0;
				
				for(int i = 1; i < constructors.size(); i++) {
					if(maxNumberOfParameters < constructors.get(i).getParameterCount()) {
						maxNumberOfParameters = constructors.get(i).getParameterCount();
						indexOfDesignatedConstructor = i;
					}
				}
				
				return Optional.of(constructors.get(indexOfDesignatedConstructor));
			}
			
			return Optional.of(constructors.get(0));
		}else
			return Optional.empty();
	}
	
	private boolean checkParametersMatch(Parameter[] parameters) {		
		for(Parameter p : parameters)
			if(!(instantiatedComponent.containsKey(p.getType().getCanonicalName()) || this.componentToInstantiate.contains(p.getType().getCanonicalName())))
				return false;
		
		return true;
	}
}
