package com.dfferentia.junit;

import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.runners.model.FrameworkMethod;

/**
 * @author Saurabh Raje
 * @email saurabh.raje@gmail.com
 * 
 * @created 02-Apr-2014
 */
public class FrameworkMethodCollection extends FrameworkMethod {
	final FrameworkMethodParams[] frameworkMethods;

	public FrameworkMethodCollection(Method method, Collection<Object[]> paramsCollection, boolean indexPresent) {
		super(method);
		// populate framework methods
		this.frameworkMethods = new FrameworkMethodParams[paramsCollection.size()];
		int index = 0;
		for (Object[] params : paramsCollection) {
			this.frameworkMethods[index] = new FrameworkMethodParams(method, params, index, indexPresent);
			index++;
		}
	}

	public FrameworkMethodParams[] getFrameworkMethods() {
		return frameworkMethods;
	}

	@Override
	public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
		// do nothing!
		return null;
	}
}
