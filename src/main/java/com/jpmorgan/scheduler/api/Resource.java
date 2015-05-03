package com.jpmorgan.scheduler.api;

/**
 * Resource to sending message
 * 
 * @author Jakub Ferschmann
 */
public interface Resource {

	/**
	 * When message was successfully sent, send next message in queue
	 * 
	 * @param message message that was sent
	 */
	void messageCompleted(MessageToSend message);

	/**
	 * When new message is received to queue and resource isn't busy, the
	 * message is sent
	 */
	void messageReceived();

}
