package com.jpmorgan.scheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	@Test
	public void testProcessMessageOneResource() throws IOException, InterruptedException {
		Gateway gateway1 = new RundomDelayGateway();

		scheduler = new Scheduler(Arrays.asList(gateway1));

		Thread thread = new Thread(scheduler);
		thread.start();

		Thread.sleep(200);

		scheduler.receive(createMessage("text1", 1L, 0));
		scheduler.receive(createMessage("text2", 1L, 10));
		scheduler.receive(createMessage("text3", 1L, 20));
		scheduler.receive(createMessage("text6", 2L, 30));
		scheduler.receive(createMessage("text4", 1L, 40));
		scheduler.receive(createMessage("text5", 1L, 50));
		scheduler.receive(createMessage("text7", 2L, 60));
		scheduler.receive(createMessage("text8", 2L, 70));
		scheduler.receive(createMessage("text9", 3L, 80));
		scheduler.receive(createMessage("text12", 4L, 90));
		scheduler.receive(createMessage("text10", 3L, 100));
		scheduler.receive(createMessage("text11", 3L, 110));
		scheduler.receive(createMessage("text15", 5L, 120));
		scheduler.receive(createMessage("text13", 4L, 130));
		scheduler.receive(createMessage("text16", 5L, 140));
		scheduler.receive(createMessage("text14", 4L, 150));

		Thread.sleep(5000);

		int order = 1;
		for (Message message : messageCaptor) {
			MessageToSend messageToSend = (MessageToSend) message;
			Assert.assertEquals("text" + order, messageToSend.getText());
			order++;
		}
	}

	// @Test
	public void testProcessMessageTwoResources() throws IOException, InterruptedException {
		Gateway gateway1 = new RundomDelayGateway();
		Gateway gateway2 = new RundomDelayGateway();

		scheduler = new Scheduler(Arrays.asList(gateway1, gateway2));

		Thread thread = new Thread(scheduler);
		thread.start();

		Thread.sleep(200);

		scheduler.receive(createMessage("text1", 1L, 0));
		scheduler.receive(createMessage("text2", 1L, 10));
		scheduler.receive(createMessage("text3", 1L, 20));
		scheduler.receive(createMessage("text6", 2L, 30));
		scheduler.receive(createMessage("text4", 1L, 40));
		scheduler.receive(createMessage("text5", 1L, 50));
		scheduler.receive(createMessage("text7", 2L, 60));
		scheduler.receive(createMessage("text8", 2L, 70));
		scheduler.receive(createMessage("text9", 3L, 80));
		scheduler.receive(createMessage("text12", 4L, 90));
		scheduler.receive(createMessage("text10", 3L, 100));
		scheduler.receive(createMessage("text11", 3L, 110));
		scheduler.receive(createMessage("text15", 5L, 120));
		scheduler.receive(createMessage("text13", 4L, 130));
		scheduler.receive(createMessage("text16", 5L, 140));
		scheduler.receive(createMessage("text14", 4L, 150));

		Thread.sleep(5000);

		int order = 1;
		for (Message message : messageCaptor) {
			MessageToSend messageToSend = (MessageToSend) message;
			Assert.assertEquals("text" + order, messageToSend.getText());
			order++;
		}
	}

	private class RundomDelayGateway implements Gateway {

		@Override
		public void send(Message message) {
			messageCaptor.add(message);

			Thread sender = new Thread(new RundomDelaySender(message));
			sender.start();
		}

	}

	private class RundomDelaySender implements Runnable {

		private final Message message;

		public RundomDelaySender(Message message) {
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
