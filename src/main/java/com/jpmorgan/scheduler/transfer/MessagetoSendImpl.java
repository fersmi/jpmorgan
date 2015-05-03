package com.jpmorgan.scheduler.transfer;

import java.util.Date;

import com.jpmorgan.scheduler.api.MessageToSend;
import com.jpmorgan.scheduler.api.Resource;
import com.jpmorgan.scheduler.api.ext.Message;

/**
 * {@inheritDoc}
 * 
 * @author Jakub Ferschmann
 */
public class MessagetoSendImpl implements Message, MessageToSend {

	private final String text;
	private final Long group;
	private Date recieveTime;
	private boolean termination;
	private boolean cancellation;

	/**
	 * Resource to sending message
	 */
	private Resource resource;

	public MessagetoSendImpl(String text, Long group) {
		this.text = text;
		this.group = group;
		termination = false;
		cancellation = false;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTermination() {
		return termination;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTermination(boolean termination) {
		this.termination = termination;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCancellation() {
		return cancellation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCancellation(boolean cancel) {
		cancellation = cancel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (cancellation ? 1231 : 1237);
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((recieveTime == null) ? 0 : recieveTime.hashCode());
		result = prime * result + (termination ? 1231 : 1237);
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MessagetoSendImpl other = (MessagetoSendImpl) obj;
		if (cancellation != other.cancellation) {
			return false;
		}
		if (group == null) {
			if (other.group != null) {
				return false;
			}
		} else if (!group.equals(other.group)) {
			return false;
		}
		if (recieveTime == null) {
			if (other.recieveTime != null) {
				return false;
			}
		} else if (!recieveTime.equals(other.recieveTime)) {
			return false;
		}
		if (termination != other.termination) {
			return false;
		}
		if (text == null) {
			if (other.text != null) {
				return false;
			}
		} else if (!text.equals(other.text)) {
			return false;
		}
		return true;
	}

}
