package com.jpmorgan.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;

import com.jpmorgan.scheduler.api.MessageToSend;
import com.jpmorgan.scheduler.api.ext.Gateway;
import com.jpmorgan.scheduler.utils.LoggerFactory;
import com.jpmorgan.scheduler.utils.MessageComparator;

/**
 * Resource scheduler to sending messages to external gateway resources.
 * 
 * @author Jakub Ferschmann
 */
public class Scheduler implements Runnable {

	private static final Logger log = LoggerFactory.getLogger();

	/**
	 * Queue with received and unsent messages. When no resources are available,
	 * messages should not be sent to the Gateway For a single resource, when
	 * two messages are received, only the first message is sent to the gateway.
	 */
	private final BlockingQueue<MessageToSend> queue;

	/**
	 * When a Termination Message is received, that means that it is the last
	 * Message in that group (not all groups have the same number of messages).
	 * If further Messages belonging to that group are received, an error should
	 * be raised.
	 */
	private final Map<Long, Boolean> terminationMessages = new HashMap<>();

	/**
	 * It should be possible to tell the scheduler that a group of messages has
	 * now been cancelled. Once cancelled, no further messages from that group
	 * should sent to the Gateway.
	 */
	private final Map<Long, Boolean> cancellationMessages = new HashMap<>();

	/**
	 * List of available resources
	 */
	private final List<AvailableResource> resources = new ArrayList<>();

	/**
	 * @param gateways list of external gateways
	 * @param queue the queue to persist and order unsent messages
	 */
	public Scheduler(List<Gateway> gateways, BlockingQueue<MessageToSend> queue) {

		// Initialize the queue
		if (queue == null) {
			this.queue = new PriorityBlockingQueue<>(100, new MessageComparator());

		} else {
			this.queue = queue;
		}

		// Resource count
		int resourcesCount = gateways.size();

		// Create resources
		for (int i = 0; i < resourcesCount; i++) {
			resources.add(new AvailableResource("Gateway resource " + i, this.queue, gateways.get(i)));
		}
	}

	/**
	 * Cancel all messages in the group
	 * 
	 * @param groupToCancel group to cancel
	 */
	public void cancelMessageGroup(Long groupToCancel) {
		log.info("Message group is canceled! {group:" + groupToCancel + "}");

		// Store information for future messages
		cancellationMessages.put(groupToCancel, Boolean.TRUE);

		// Cancel all message in the queue
		for (Object object : queue.toArray()) {
			MessageToSend message = (MessageToSend) object;
			if (message.getGroup().equals(groupToCancel)) {
				message.setCancellation(Boolean.TRUE);
			}
		}
	}

	/**
	 * Receive and put message to queue
	 * 
	 * @param message message to send
	 * @throws InterruptedException if receiving failed
	 */
	public void receive(MessageToSend message) throws InterruptedException {
		log.info("Message received! {text:" + message.getText() + " group:" + message.getGroup() + "}");

		// Check if message group is finished
		if (terminationMessages.containsKey(message.getGroup())) {
			throw new RuntimeException("Message received from group that was finished!");
		}

		// Check if group is canceled, if yes than skip message
		if (cancellationMessages.containsKey(message.getGroup())) {
			return;
		}

		// If the message is the last one, store this information to the map
		if (message.isTermination()) {
			terminationMessages.put(message.getGroup(), Boolean.TRUE);
		}

		message.setRecieveTime(new Date());
		queue.put(message);
	}

	/**
	 * Run all resources to sending messages *
	 */
	@Override
	public void run() {
		for (AvailableResource resource : resources) {
			// Start resource
			resource.start();
		}
	}

	/**
	 * Return the queue that orders messages for sending
	 * 
	 * @return
	 */
	public BlockingQueue<MessageToSend> getQueue() {
		return queue;
	}

	/**
	 * Return all gateway resources
	 * 
	 * @return
	 */
	public List<AvailableResource> getResources() {
		return resources;
	}

}
