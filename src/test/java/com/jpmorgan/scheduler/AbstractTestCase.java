package com.jpmorgan.scheduler;

import java.util.Calendar;
import java.util.Date;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.jpmorgan.scheduler.api.MessageToSend;
import com.jpmorgan.scheduler.api.ext.Message;
import com.jpmorgan.scheduler.transfer.MessagetoSendImpl;

/**
 * Shared test case for unit tests
 * 
 * @author Jakub Ferschmann
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractTestCase {

	/**
	 * Create current time with extra milliseconds
	 * 
	 * @param addMillis
	 * @return
	 */
	protected Date createTime(int addMillis) {
		Calendar time = Calendar.getInstance();
		time.add(Calendar.MILLISECOND, addMillis);
		return time.getTime();
	}

	/**
	 * Create {@link Message} for tests
	 * 
	 * @param text
	 * @param group
	 * @param addMillis
	 * @return
	 */
	protected MessageToSend createMessage(String text, Long group, int addMillis) {
		MessageToSend message = new MessagetoSendImpl(text, group);
		message.setRecieveTime(createTime(addMillis));
		return message;
	}

}
