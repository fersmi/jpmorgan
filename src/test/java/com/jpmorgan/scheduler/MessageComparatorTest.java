package com.jpmorgan.scheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.jpmorgan.scheduler.api.MessageToSend;
import com.jpmorgan.scheduler.utils.MessageComparator;

/**
 * Test scenario for {@link MessageComparator}
 * 
 * @author Jakub Ferschmann
 */
public class MessageComparatorTest extends AbstractTestCase {

	private final MessageComparator comparator = new MessageComparator();

	/**
	 * Test correct order by comparator
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testCompare() throws IOException, InterruptedException {
		List<MessageToSend> messages = new ArrayList<>();
		messages.add(createMessage("text1", 1L, 0, false));
		messages.add(createMessage("text5", 2L, 10, false));
		messages.add(createMessage("text7", 3L, 20, false));
		messages.add(createMessage("text2", 1L, 30, false));
		messages.add(createMessage("text3", 1L, 40, false));
		messages.add(createMessage("text6", 2L, 50, false));
		messages.add(createMessage("text4", 1L, 60, false));
		messages.add(createMessage("text8", 3L, 70, false));

		Collections.sort(messages, comparator);

		int count = 1;
		for (MessageToSend message : messages) {
			Assert.assertEquals("text" + count, message.getText());
			count++;
		}
	}

}
