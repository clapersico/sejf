package org.sejf.core.dependencies;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.sejf.core.components.ComponentScan;
import org.sejf.core.components.dependencies.DependencyTree;
import org.sejf.core.components.scanstrategy.FixedPathPackageScanStrategy;

public class DependencyTreeTest {
	
	@Test
	void checkDependenciesTree() {
		ComponentScan cs = new ComponentScan(new FixedPathPackageScanStrategy("test.stub"));
		Optional<DependencyTree> dependencyTreeOpt = cs.scan();
		
		assert(dependencyTreeOpt.isPresent());
		
		DependencyTree dependencyTree = dependencyTreeOpt.get();
		
		assert(dependencyTree.get("test.stub.StubClassA").get().size() == 1);
		assert(dependencyTree.get("test.stub.StubClassB").isPresent());
	}
}
