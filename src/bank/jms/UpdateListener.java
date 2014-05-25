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
				handler.accountChanged(((TextMessage) arg0).getText());
				System.out.println("Account number " + ((TextMessage) arg0).getText() + " changed");
			} catch (IOException | JMSException e) {
				System.err.println("Unable to get text from message.");
			}
	}

}
