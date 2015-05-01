package com.jpmorgan.scheduler.transfer;

import java.util.Date;

import com.jpmorgan.scheduler.api.MessageToSend;
import com.jpmorgan.scheduler.api.Resource;
import com.jpmorgan.scheduler.api.ext.Message;

/**
 * Information about message to send
 * 
 * @author Jakub Ferschmann
 */
public class MessagetoSendImpl implements Message, MessageToSend {

	private final String text;
	private final Long group;
	private Date recieveTime;

	/**
	 * Resource to sending message
	 */
	private Resource resource;

	public MessagetoSendImpl(String text, Long group) {
		this.text = text;
		this.group = group;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void completed() {
		resource.messageCompleted(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRecieveTime(Date recieveTime) {
		this.recieveTime = recieveTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getGroup() {
		return group;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getRecieveTime() {
		return recieveTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText() {
		return text;
	}

}
