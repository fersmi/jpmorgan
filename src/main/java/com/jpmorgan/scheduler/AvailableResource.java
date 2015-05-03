package com.jpmorgan.scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;

import com.jpmorgan.scheduler.api.MessageToSend;
import com.jpmorgan.scheduler.api.Resource;
import com.jpmorgan.scheduler.api.ext.Gateway;
import com.jpmorgan.scheduler.utils.LoggerFactory;

/**
 * Represents resource of gateway from external systems
 * 
 * @author Jakub Ferschmann
 */
public class AvailableResource extends Thread implements Resource {

	private static final Logger log = LoggerFactory.getLogger();

	/**
	 * Monitor to solve concurrent issues
	 */
	public final Lock monitor = new ReentrantLock();
	public final Condition notToDo = monitor.newCondition();

	/**
	 * Queue with messages
	 */
	private final BlockingQueue<MessageToSend> queue;
	/**
	 * External gateway to sending messages
	 */
	private final Gateway gateway;

	/**
	 * If not null indicates that resource sending message
	 */
	private boolean canSendMessage = true;

	public AvailableResource(String threadName, BlockingQueue<MessageToSend> queue, Gateway gateway) {
		super(threadName);
		this.queue = queue;
		this.gateway = gateway;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		while (true) {
			monitor.lock();

			try {

				// Is resource busy?
				if (canSendMessage) {

					// Take next message from queue
					MessageToSend message = queue.take();

					if (message != null) {
						// If message is canceled? Skip it.
						if (message.isCancellation()) {
							continue;
						}

						canSendMessage = false;
						// Set resource to notify if sending finished
						message.setResource(this);

						log.info("Sending message started! {text:" + message.getText() + " group:" + message.getGroup() + "}");

						// Send message to external gateway
						gateway.send(message);
					}
				}

				notToDo.await();

			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);

			} finally {
				monitor.unlock();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void messageCompleted(MessageToSend message) {
		monitor.lock();

		try {
			log.info("Sending message completed! {text:" + message.getText() + " group:" + message.getGroup() + "}");

			canSendMessage = true;
			notToDo.signal();

		} finally {
			monitor.unlock();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void messageReceived() {
		monitor.lock();

		try {

			notToDo.signal();

		} finally {
			monitor.unlock();
		}
	}

}
