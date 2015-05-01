package com.jpmorgan.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;

import com.jpmorgan.scheduler.api.MessageToSend;
import com.jpmorgan.scheduler.api.ext.Gateway;
import com.jpmorgan.scheduler.utils.LoggerFactory;
import com.jpmorgan.scheduler.utils.MessageComparator;

/**
 * Resource scheduler to sending
 * 
 * @author Jakub Ferschmann
 */
public class Scheduler implements Runnable {

	private static final Logger log = LoggerFactory.getLogger();

	/**
	 * Queue with received and unsent messages
	 */
	private final BlockingQueue<MessageToSend> queue = new PriorityBlockingQueue<>(100, new MessageComparator());

	/**
	 * List of available resources
	 */
	private final List<AvailableResource> resources = new ArrayList<>();

	/**
	 * @param gateways list of external gateways
	 */
	public Scheduler(List<Gateway> gateways) {
		// Resource count
		int resourcesCount = gateways.size();

		// Create resources
		for (int i = 0; i < resourcesCount; i++) {
			resources.add(new AvailableResource("Gateway resource " + i, queue, gateways.get(i)));
		}
	}

	/**
	 * Receive and put message to queue
	 * 
	 * @param message message to send
	 * @throws InterruptedException if receiving faild
	 */
	public void receive(MessageToSend message) throws InterruptedException {
		log.info("Message received! {text:" + message.getText() + " group:" + message.getGroup() + "}");

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
}
