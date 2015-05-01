package com.jpmorgan.scheduler.utils;

import org.slf4j.Logger;

/**
 * Convenient class to simplify logger instantiation.
 * 
 * This is just a temporary location, should be moved.
 * 
 * @author Jakub Ferschmann
 */
public class LoggerFactory {

	/**
	 * The name of this class used to search the stack
	 */
	private static final String THIS_CLASS_NAME = LoggerFactory.class.getName();

	/**
	 * Returns logger for the invoking class. There is not need to specify the
	 * class name, it is automatically read from current stack.
	 * 
	 * @return Logger for the invoking class.
	 */
	public static Logger getLogger() {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		// Find current classname in the stack and return the following
		// classname in the stack
		for (int i = 0; i < elements.length - 1; i++) {
			if (THIS_CLASS_NAME.equals(elements[i].getClassName())) {
				return getLogger(elements[i + 1].getClassName());
			}
		}
		return getLogger("loggerUnknowClass");
	}

	/**
	 * Returns logger by its name.
	 * 
	 * @param name logger name
	 * @return Logger for the given name.
	 */
	public static Logger getLogger(String name) {
		return org.slf4j.LoggerFactory.getLogger(name);
	}

}
