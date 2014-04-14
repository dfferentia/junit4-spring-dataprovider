package com.dfferentia.junit;

import java.lang.reflect.Method;

import org.junit.runners.model.FrameworkMethod;

/**
 * @author Saurabh Raje
 * @email saurabh.raje@gmail.com
 * 
 * @created 02-Apr-2014
 */
public class FrameworkMethodParams extends FrameworkMethod {
	final int index;
	final boolean indexPresent;
	final Object[] params;

	public FrameworkMethodParams(Method method, Object[] params, int index, boolean indexPresent) {
		super(method);
		this.index = index;
		this.indexPresent = indexPresent;
		this.params = new Object[params.length + (indexPresent ? 1 : 0)];
		System.arraycopy(params, 0, this.params, (indexPresent ? 1 : 0), params.length);
		if (indexPresent) {
			this.params[0] = index;
		}
	}

	public void display(StringBuilder sb) {
		sb.append("[");
		sb.append(index);
		sb.append("]");
		sb.append("[");
		int initialIndex = indexPresent ? 1 : 0;
		for (int i = initialIndex; i < params.length; ++i) {
			if (i > initialIndex) {
				sb.append(", ");
			}
			sb.append(params[i]);
		}
		sb.append("]");
	}

	@Override
	public Object invokeExplosively(final Object target, final Object... params) throws Throwable {
		return super.invokeExplosively(target, this.params);
	}
}
