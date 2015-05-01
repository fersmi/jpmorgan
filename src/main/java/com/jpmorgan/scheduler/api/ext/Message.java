package com.jpmorgan.scheduler.api.ext;

/**
 * Interface of external message
 * 
 * @author Jakub Ferschmann
 */
public interface Message {

	/**
	 * When a Message has completed processing, its completed() method will be
	 * called
	 */
	void completed();
}
