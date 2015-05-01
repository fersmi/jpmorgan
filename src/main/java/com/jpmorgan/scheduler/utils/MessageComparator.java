package com.jpmorgan.scheduler.utils;

import java.util.Comparator;

import com.jpmorgan.scheduler.api.MessageToSend;

/**
 * Comparator to order messages in queue. First order by group and after by
 * recieve time
 * 
 * @author Jakub Ferschmann
 */
public class MessageComparator implements Comparator<MessageToSend> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(MessageToSend message1, MessageToSend message2) {
		if (message1.getGroup().equals(message2.getGroup())) {
			return message1.getRecieveTime().compareTo(message2.getRecieveTime());
		}
		return message1.getGroup().compareTo(message2.getGroup());
	}

}
