package org.sejf.core.components.exceptions;

import org.sejf.core.components.dependencies.DependencyTree;

public class NotInstantiableComponentException extends ApplicationNotStartableException {
	private static final long serialVersionUID = -8597618424101070169L;
	
	private String[] componentsName;
	
	public NotInstantiableComponentException(String... componentsName) {
		this.componentsName = componentsName;
	}
	
	public NotInstantiableComponentException(DependencyTree depTree) {	
		this.componentsName = new String[depTree.getAllComponents().size()];
		depTree.getAllComponents().toArray(this.componentsName);
	}
	
	@Override
	public String getLocalizedMessage() {
		String components = "";
		
		for(String component : this.componentsName)
			components += component + " - ";
		
		components = components.substring(0, components.length()-2);
		
		return String.format("Cannot instantiate the following components: %s", components);
	}
}
