package com.jpmorgan.scheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jpmorgan.scheduler.api.MessageToSend;
import com.jpmorgan.scheduler.api.ext.Gateway;
import com.jpmorgan.scheduler.api.ext.Message;

/**
 * Test scenarios for {@link Scheduler}
 * 
 * @author Jakub Ferschmann
 */
public class SchedulerTest extends AbstractTestCase {

	private Scheduler scheduler;

	private List<Message> messageCaptor;

	@Before
	public void setUp() {
		messageCaptor = new ArrayList<>();
	}

	/**
	 * Test default (by group) type of ordering messages in queue
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testProcessMessageDefaultOrdering() throws IOException, InterruptedException {
		Gateway gateway1 = new RandomDelayGateway();

		scheduler = new Scheduler(Arrays.asList(gateway1), null);

		Thread thread = new Thread(scheduler);
		thread.start();

		Thread.sleep(200);

		scheduler.receive(createMessage("text1", 1L, 0, false));
		scheduler.receive(createMessage("text2", 1L, 10, false));
		scheduler.receive(createMessage("text3", 1L, 20, false));
		scheduler.receive(createMessage("text6", 2L, 30, false));
		scheduler.receive(createMessage("text4", 1L, 40, false));
		scheduler.receive(createMessage("text5", 1L, 50, false));
		scheduler.receive(createMessage("text7", 2L, 60, false));
		scheduler.receive(createMessage("text8", 2L, 70, false));
		scheduler.receive(createMessage("text9", 3L, 80, false));
		scheduler.receive(createMessage("text12", 4L, 90, false));
		scheduler.receive(createMessage("text10", 3L, 100, false));
		scheduler.receive(createMessage("text11", 3L, 110, false));
		scheduler.receive(createMessage("text15", 5L, 120, false));
		scheduler.receive(createMessage("text13", 4L, 130, false));
		scheduler.receive(createMessage("text16", 5L, 140, false));
		scheduler.receive(createMessage("text14", 4L, 150, false));

		while (scheduler.getQueue().size() > 0) {
			Thread.sleep(300);
		}

		Assert.assertEquals(16, messageCaptor.size());
		Assert.assertEquals(0, scheduler.getQueue().size());

		int order = 1;
		for (Message message : messageCaptor) {
			MessageToSend messageToSend = (MessageToSend) message;
			Assert.assertEquals("text" + order, messageToSend.getText());
			order++;
		}
	}

	/**
	 * Test different (FIFO) type of ordering messages in queue
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testProcessMessageFiFoOrdering() throws IOException, InterruptedException {
		Gateway gateway1 = new RandomDelayGateway();

		scheduler = new Scheduler(Arrays.asList(gateway1), new ArrayBlockingQueue<MessageToSend>(100));

		Thread thread = new Thread(scheduler);
		thread.start();

		Thread.sleep(200);

		scheduler.receive(createMessage("text1", 1L, 0, false));
		scheduler.receive(createMessage("text2", 1L, 10, false));
		scheduler.receive(createMessage("text3", 1L, 20, false));
		scheduler.receive(createMessage("text4", 2L, 30, false));
		scheduler.receive(createMessage("text5", 1L, 40, false));
		scheduler.receive(createMessage("text6", 1L, 50, false));
		scheduler.receive(createMessage("text7", 2L, 60, false));
		scheduler.receive(createMessage("text8", 2L, 70, false));
		scheduler.receive(createMessage("text9", 3L, 80, false));
		scheduler.receive(createMessage("text10", 4L, 90, false));
		scheduler.receive(createMessage("text11", 3L, 100, false));
		scheduler.receive(createMessage("text12", 3L, 110, false));
		scheduler.receive(createMessage("text13", 5L, 120, false));
		scheduler.receive(createMessage("text14", 4L, 130, false));
		scheduler.receive(createMessage("text15", 5L, 140, false));
		scheduler.receive(createMessage("text16", 4L, 150, false));

		while (scheduler.getQueue().size() > 0) {
			Thread.sleep(300);
		}

		Assert.assertEquals(16, messageCaptor.size());
		Assert.assertEquals(0, scheduler.getQueue().size());

		int order = 1;
		for (Message message : messageCaptor) {
			MessageToSend messageToSend = (MessageToSend) message;
			Assert.assertEquals("text" + order, messageToSend.getText());
			order++;
		}
	}

	/**
	 * Test scenario with two resources
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testProcessMessageTwoResources() throws IOException, InterruptedException {
		Gateway gateway1 = new RandomDelayGateway();
		Gateway gateway2 = new RandomDelayGateway();

		scheduler = new Scheduler(Arrays.asList(gateway1, gateway2), null);

		Thread thread = new Thread(scheduler);
		thread.start();

		Thread.sleep(200);

		scheduler.receive(createMessage("text1", 1L, 0, false));
		scheduler.receive(createMessage("text2", 1L, 10, false));
		scheduler.receive(createMessage("text3", 1L, 20, false));
		scheduler.receive(createMessage("text6", 2L, 30, false));
		scheduler.receive(createMessage("text4", 1L, 40, false));
		scheduler.receive(createMessage("text5", 1L, 50, false));
		scheduler.receive(createMessage("text7", 2L, 60, false));
		scheduler.receive(createMessage("text8", 2L, 70, false));
		scheduler.receive(createMessage("text9", 3L, 80, false));
		scheduler.receive(createMessage("text12", 4L, 90, false));
		scheduler.receive(createMessage("text10", 3L, 100, false));
		scheduler.receive(createMessage("text11", 3L, 110, false));
		scheduler.receive(createMessage("text15", 5L, 120, false));
		scheduler.receive(createMessage("text13", 4L, 130, false));
		scheduler.receive(createMessage("text16", 5L, 140, false));
		scheduler.receive(createMessage("text14", 4L, 150, false));

		while (scheduler.getQueue().size() > 0) {
			Thread.sleep(300);
		}

		Assert.assertEquals(16, messageCaptor.size());
		Assert.assertEquals(0, scheduler.getQueue().size());
	}

	/**
	 * Test scenario with five resources
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testProcessMessageFiveResources() throws IOException, InterruptedException {
		Gateway gateway1 = new RandomDelayGateway();
		Gateway gateway2 = new RandomDelayGateway();
		Gateway gateway3 = new RandomDelayGateway();
		Gateway gateway4 = new RandomDelayGateway();
		Gateway gateway5 = new RandomDelayGateway();

		scheduler = new Scheduler(Arrays.asList(gateway1, gateway2, gateway3, gateway4, gateway5), null);

		Thread thread = new Thread(scheduler);
		thread.start();

		Thread.sleep(200);

		scheduler.receive(createMessage("text1", 1L, 0, false));
		scheduler.receive(createMessage("text2", 1L, 10, false));
		scheduler.receive(createMessage("text3", 1L, 20, false));
		scheduler.receive(createMessage("text6", 2L, 30, false));
		scheduler.receive(createMessage("text4", 1L, 40, false));
		scheduler.receive(createMessage("text5", 1L, 50, false));
		scheduler.receive(createMessage("text7", 2L, 60, false));
		scheduler.receive(createMessage("text8", 2L, 70, false));
		scheduler.receive(createMessage("text9", 3L, 80, false));
		scheduler.receive(createMessage("text12", 4L, 90, false));
		scheduler.receive(createMessage("text10", 3L, 100, false));
		scheduler.receive(createMessage("text11", 3L, 110, false));
		scheduler.receive(createMessage("text15", 5L, 120, false));
		scheduler.receive(createMessage("text13", 4L, 130, false));
		scheduler.receive(createMessage("text16", 5L, 140, false));
		scheduler.receive(createMessage("text14", 4L, 150, false));

		while (scheduler.getQueue().size() > 0) {
			Thread.sleep(300);
		}

		Assert.assertEquals(16, messageCaptor.size());
		Assert.assertEquals(0, scheduler.getQueue().size());
	}

	/**
	 * Test scenario that message is received in group that was finished.
	 * Exception is thrown
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test(expected = RuntimeException.class)
	public void testProcessMessageTermination() throws IOException, InterruptedException {
		Gateway gateway1 = new RandomDelayGateway();

		scheduler = new Scheduler(Arrays.asList(gateway1), null);

		Thread thread = new Thread(scheduler);
		thread.start();

		Thread.sleep(200);

		scheduler.receive(createMessage("text1", 1L, 0, false));
		scheduler.receive(createMessage("text2", 1L, 10, false));
		scheduler.receive(createMessage("text3", 1L, 20, false));
		scheduler.receive(createMessage("text6", 2L, 30, false));
		scheduler.receive(createMessage("text4", 1L, 40, false));
		scheduler.receive(createMessage("text5", 1L, 50, false));
		scheduler.receive(createMessage("text7", 2L, 60, false));
		scheduler.receive(createMessage("text8", 2L, 70, false));
		scheduler.receive(createMessage("text9", 3L, 80, false));
		scheduler.receive(createMessage("text12", 4L, 90, false));
		scheduler.receive(createMessage("text10", 3L, 100, true));
		scheduler.receive(createMessage("text11", 3L, 110, false));

	}

	/**
	 * Test scenario that one group of message is canceled. All unsent messages
	 * aren't send
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test()
	public void testProcessMessageCancellation() throws IOException, InterruptedException {
		Gateway gateway1 = new RandomDelayGateway();

		scheduler = new Scheduler(Arrays.asList(gateway1), null);

		Thread thread = new Thread(scheduler);
		thread.start();

		Thread.sleep(200);

		scheduler.receive(createMessage("text1", 1L, 0, false));
		scheduler.receive(createMessage("text2", 1L, 10, false));
		scheduler.receive(createMessage("text3", 1L, 20, false));
		scheduler.receive(createMessage("text6", 2L, 30, false));
		scheduler.receive(createMessage("text4", 1L, 40, false));
		scheduler.receive(createMessage("text5", 1L, 50, false));
		scheduler.receive(createMessage("text7", 2L, 60, false));
		scheduler.receive(createMessage("text8", 2L, 70, false));
		scheduler.receive(createMessage("not send", 3L, 80, false));
		scheduler.receive(createMessage("text9", 4L, 90, false));
		scheduler.receive(createMessage("not send", 3L, 100, false));

		// Cancel messages in group 3
		scheduler.cancelMessageGroup(3L);

		scheduler.receive(createMessage("not send", 3L, 110, false));
		scheduler.receive(createMessage("text12", 5L, 120, false));
		scheduler.receive(createMessage("text10", 4L, 130, false));
		scheduler.receive(createMessage("text13", 5L, 140, false));
		scheduler.receive(createMessage("text11", 4L, 150, false));

		while (scheduler.getQueue().size() > 0) {
			Thread.sleep(300);
		}

		Assert.assertEquals(13, messageCaptor.size());
		Assert.assertEquals(0, scheduler.getQueue().size());

		int order = 1;
		for (Message message : messageCaptor) {
			MessageToSend messageToSend = (MessageToSend) message;
			Assert.assertEquals("text" + order, messageToSend.getText());
			order++;
		}

	}

	/**
	 * Gateway with random delay
	 * 
	 * @author Jakub Ferschmann
	 */
	private class RandomDelayGateway implements Gateway {

		@Override
		public void send(Message message) {
			messageCaptor.add(message);

			Thread sender = new Thread(new RandomDelaySender(message));
			sender.start();
		}

	}

	/**
	 * Sender with random delay
	 * 
	 * @author Jakub Ferschmann
	 */
	private class RandomDelaySender implements Runnable {

		private final Message message;

		public RandomDelaySender(Message message) {
			super();
			this.message = message;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			try {
				Thread.sleep((long) (Math.random() * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			message.completed();
		}
	}
}
