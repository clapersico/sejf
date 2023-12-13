package org.sejf.core.reflections;

import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.sejf.core.components.singleton.Singleton;

public class ReflectionsUtil {	
	public static Set<Class<?>> scanPackage(String basePackage) {	
		Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated);
		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Singleton.class);
		
		return classes.stream()
				.map(clazz -> getClass(clazz.getCanonicalName()))
				.filter(clazz -> clazz != null)
				.collect(Collectors.toSet());
	}
	
	private static Class<?> getClass(String fullClassName) {
		try {
			return Class.forName(fullClassName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}
