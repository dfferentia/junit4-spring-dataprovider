package com.dfferentia.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Saurabh Raje
 * @email saurabh.raje@gmail.com
 * 
 * @created 02-Apr-2014
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataProvider {
	Class<?> clazz();

	/**
	 * Any no-arg static method which returns Collection<Object[]>
	 * 
	 * @return
	 */
	String method();

	/**
	 * Should the index of iteration be passed as the FIRST parameter to the @Test
	 * method?
	 * 
	 * @return
	 */
	boolean index() default false;
}
