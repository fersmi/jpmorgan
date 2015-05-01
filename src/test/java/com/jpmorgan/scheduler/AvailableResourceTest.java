package com.jpmorgan.scheduler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jpmorgan.scheduler.api.MessageToSend;
import com.jpmorgan.scheduler.api.ext.Gateway;
import com.jpmorgan.scheduler.api.ext.Message;

/**
 * Test scenarios for {@link AvailableResource}
 * 
 * @author Jakub Ferschmann
 */
public class AvailableResourceTest extends AbstractTestCase {

	@Mock
	private BlockingQueue<MessageToSend> queue;

	@Mock
	private Gateway gateway;

	private AvailableResource resource;

	@Before
	public void setUp() {
		reset(gateway, queue);
		resource = new AvailableResource("Gateway resource", queue, gateway);
	}

	@Test
	public void testProcessMessage() throws IOException, InterruptedException {

		MessageToSend message = createMessage("text", 1L, 0);
		when(queue.take()).thenReturn(message);

		// get the first message from queue and send
		resource.start();

		Thread.sleep(500);
		verify(gateway).send(any(Message.class));

		// Message was send, take next one to send
		resource.messageCompleted(message);

		Thread.sleep(500);
		verify(gateway, times(2)).send(any(Message.class));

		// New message received, but resource is busy
		resource.messageReceived();

		Thread.sleep(500);
		verify(gateway, times(2)).send(any(Message.class));

	}

}
