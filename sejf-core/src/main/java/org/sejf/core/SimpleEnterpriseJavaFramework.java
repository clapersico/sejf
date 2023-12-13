package org.sejf.core;

import java.util.Optional;

import org.sejf.core.components.ComponentManager;
import org.sejf.core.components.ComponentScan;
import org.sejf.core.components.dependencies.DependencyTree;
import org.sejf.core.components.scanstrategy.FixedPathPackageScanStrategy;
import org.sejf.core.context.SEJFContext;

public class SimpleEnterpriseJavaFramework {
	public static SEJFContext load() {
		return _load(Optional.empty());
	}
	
	public static SEJFContext load(String basePackage) {
		return _load(Optional.of(basePackage));
	}
	
	private static SEJFContext _load(Optional<String> basePackage) {
		ComponentScan cs;
		ComponentManager cm = new ComponentManager();
		
		if(basePackage.isPresent())
			cs = new ComponentScan(new FixedPathPackageScanStrategy(basePackage.get()));
		else
			cs = new ComponentScan();
		
		Optional<DependencyTree> dependencyTreeOptional = cs.scan();
		
		if(dependencyTreeOptional.isPresent())
			cm.instantiateComponent(dependencyTreeOptional.get());
		
		return new SEJFContext(cm);
	}
}
