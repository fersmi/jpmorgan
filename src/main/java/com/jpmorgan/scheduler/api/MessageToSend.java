package com.jpmorgan.scheduler.api;

import java.util.Date;

import com.jpmorgan.scheduler.api.ext.Message;

/**
 * Message information to send by external gateway
 * 
 * @author Jakub Ferschmann
 */
public interface MessageToSend extends Message {

	/**
	 * Return text of the message
	 * 
	 * @return message text
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
	 * @return name of the group
	 */
	Long getGroup();

	/**
	 * Get time when the message was received
	 * 
	 * @return receive time
	 */
	Date getRecieveTime();

	/**
	 * Set time when the message was received
	 * 
	 * @param recieveTime receive time
	 */
	void setRecieveTime(Date recieveTime);

	/**
	 * When a Termination Message is received, that means that it is the last
	 * Message in that group (not all groups have the same number of messages).
	 * If further Messages belonging to that group are received, an error should
	 * be raised.
	 * 
	 * @return true if it is last message in group, otherwise false
	 */
	boolean isTermination();

	/**
	 * Set message that is the last one in the group
	 * 
	 * @param termination indicates termination
	 */
	void setTermination(boolean termination);

	/**
	 * It should be possible to tell the scheduler that a group of messages has
	 * now been cancelled. Once cancelled, no further messages from that group
	 * should sent to the Gateway.
	 * 
	 * @return true if all messages in the group is canceled
	 */
	boolean isCancellation();

	/**
	 * Set that message is cancel
	 * 
	 * @param cancel indicates that message is canceled
	 */
	void setCancellation(boolean cancel);
}
