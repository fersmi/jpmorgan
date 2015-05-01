package com.jpmorgan.scheduler.api;

import java.util.Date;

import com.jpmorgan.scheduler.api.ext.Message;

/**
 * Message information to sending by external gateway
 * 
 * @author Jakub Ferschmann
 */
public interface MessageToSend extends Message {

	/**
	 * Return text of the message
	 * 
	 * @return
	 */
	String getText();

	/**
	 * Set resource for sending messages via external gateway
	 * 
	 * @param resource
	 */
	void setResource(Resource resource);

	/**
	 * Return name of the message group
	 * 
	 * @return
	 */
	Long getGroup();

	/**
	 * Get time when the message was received
	 * 
	 * @return
	 */
	Date getRecieveTime();

	/**
	 * Set time when the message was received
	 * 
	 * @param recieveTime
	 */
	void setRecieveTime(Date recieveTime);
}
