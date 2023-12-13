package org.sejf.core.components;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.sejf.core.components.dependencies.DependencyTree;
import org.sejf.core.components.exceptions.ApplicationNotStartableException;
import org.sejf.core.components.exceptions.NotInstantiableComponentException;
import org.sejf.core.components.scanstrategy.PackageScanStrategy;
import org.sejf.core.components.scanstrategy.StandardPathScanStrategy;
import org.sejf.core.reflections.ReflectionsUtil;

public class ComponentScan {
	private PackageScanStrategy packageScanStrategy;
	private Set<Class<?>> classes;
	
	public ComponentScan() {
		this.packageScanStrategy = new StandardPathScanStrategy();
	}
	
	public ComponentScan(PackageScanStrategy packageScanStrategy) {
		this.packageScanStrategy = packageScanStrategy;
	}
	
	public Optional<DependencyTree> scan() {
		try {
			return Optional.of(this._scan());
		} catch(ApplicationNotStartableException ex) {
			ex.printStackTrace();
		}
		
		return Optional.empty();
	}
	
	private DependencyTree _scan() {
		this.classes = ReflectionsUtil.scanPackage(this.packageScanStrategy.getPackage());
		DependencyTree dependencyTree = new DependencyTree();
		
		for(Class<?> clazz : this.classes) {
			dependencyTree.add(this.identifyParametersToInject(clazz));	
		}
		
		return dependencyTree;
	}

	private Component identifyParametersToInject(Class<?> clazz) {
		Component component = new Component(clazz.getCanonicalName());
		Constructor<?>[] constructors = clazz.getConstructors();

		for (Constructor<?> constructor : constructors) {
			Optional<List<String>> dependencies = this.analyzeConstructor(component, constructor);
			
			if(dependencies.isPresent())
				dependencies.get().stream().forEach(dep -> component.addDependency(dep));
		}
		
		if(component.getDependencies().size() == 0)
			if(component.hasEmptyConstructor())
				component.addDependency("");
			else
				throw new NotInstantiableComponentException(component.getComponentName());
		
		return component;
	}
	
	private Optional<List<String>> analyzeConstructor(Component component, Constructor<?> constructor) {
		if (constructor.getParameterCount() == 0)
			component.hasEmptyConstructor(true);
		
		List<String> dependencies = new ArrayList<>();

		for (Parameter parameter : constructor.getParameters()) {
			if(parameter.getType().isPrimitive())
				return Optional.empty();
			
			dependencies.add(parameter.getType().getCanonicalName());
		}
		
		return Optional.of(dependencies);
	}
}
