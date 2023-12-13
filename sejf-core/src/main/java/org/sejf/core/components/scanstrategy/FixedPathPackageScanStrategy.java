package org.sejf.core.components.scanstrategy;

public class FixedPathPackageScanStrategy implements PackageScanStrategy {
	private String path;
	
	public FixedPathPackageScanStrategy(String path) {
		this.path = path;
	}
	
	@Override
	public String getPackage() {
		return this.path;
	}
}
