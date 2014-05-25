package bank.jms;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import bank.BankDriver2.UpdateHandler;

public class UpdateListener implements MessageListener {

	private UpdateHandler handler;
	
	public UpdateListener(UpdateHandler h) {
		handler = h;
	}
	
	@Override
	public void onMessage(Message arg0) {
		if (arg0 instanceof TextMessage)
			try {
				if (arg0.getJMSCorrelationID().equals("ALL"))
					System.out.println("Account " + ((TextMessage) arg0).getText() + " changed");
					handler.accountChanged(((TextMessage) arg0).getText());
			} catch (IOException | JMSException e) {
				System.err.println("Unable to get text from message.");
			}
	}

}
