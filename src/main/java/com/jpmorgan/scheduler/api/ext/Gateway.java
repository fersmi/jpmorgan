package com.jpmorgan.scheduler.api.ext;

/**
 * Interface of external gateway
 * 
 * @author Jakub Ferschmann
 */
public interface Gateway {

	/**
	 * send messages to be processed by calling the Gateway's send(Message msg)
	 * method
	 * 
	 * @param message message to send
	 */
	void send(Message message);

}
