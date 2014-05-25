package bank.jms;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.management.JMRuntimeException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.commands.Command;
import bank.common.ServerBank;


public class JmsServer {
	
	protected static Bank bank;
	protected static JMSProducer updateTopic;
	protected static JMSConsumer operationsQueue;
	private static JMSContext context;
	private static Queue queue;
	private static Topic topic;

	public static void main(String[] args) throws NamingException {
		final Context jndiContext = new InitialContext();
		final ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
		queue = (Queue) jndiContext.lookup("BANK");
		topic = (Topic) jndiContext.lookup("BANK.LISTENER");
		
		System.out.println("JMS-Resources bound.");
		
		bank = new JmsBank();
		
		System.out.println("Bank initialized.");
		
		context = factory.createContext();
		updateTopic = factory.createContext().createProducer();
		operationsQueue = factory.createContext().createConsumer(queue);
		
		System.out.println("Producer & consumer initialized.\nServer ready.");
		
		ObjectMessage message, response;
		Command cmd;
		String handlerID;
		
		while (true) {
			message = (ObjectMessage) operationsQueue.receive();
			try {
				handlerID = message.getJMSCorrelationID();
				cmd = (Command) message.getObject();
				Serializable o = (Serializable) cmd.execute(bank);
				response = context.createObjectMessage(o);
				updateTopic.setJMSCorrelationID(handlerID);
				updateTopic.send(topic, response);
				System.out.println("message sent to " + response.getJMSCorrelationID());
			} catch (JMSException e) {
				System.err.println("Not a valid command received.");
			} catch (IOException e) {
				System.err.println("Command could not be executed.");
			}
		}
		
	}
	
	static class JmsBank implements Bank {
		
		private Bank bank;
		
		public JmsBank() {
			bank = new ServerBank();
		}

		@Override
		public String createAccount(String owner) throws IOException {
			System.out.println("Create account for " + owner);
			String accNumber = bank.createAccount(owner);
			sendUpdates(accNumber);
			return accNumber;
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			System.out.println("Close account #" + number);
			boolean success = bank.closeAccount(number);
			sendUpdates(number);
			return success;
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			System.out.println("Get account numbers");
			return bank.getAccountNumbers();
		}

		@Override
		public Account getAccount(String number) throws IOException {
			Account ret = bank.getAccount(number);
			return ret;
		}

		@Override
		public void transfer(Account a, Account b, double amount) throws IOException,
				IllegalArgumentException, OverdrawException, InactiveException {
			System.out.println("Transfer " + amount + " from " + a.getNumber() + " to " + b.getNumber());
			bank.transfer(a, b, amount);
			sendUpdates(a.getNumber());
			sendUpdates(b.getNumber());
		}
		
		private void sendUpdates(String number) {
			updateTopic.setJMSCorrelationID("ALL");
			TextMessage m = context.createTextMessage(number);
			updateTopic.send(topic, m);
			try {
				System.out.println(m.getJMSCorrelationID() + " clients notified");
			} catch (JMSException e) {
				System.err.println("asdf");
			}
		}
	}

}

