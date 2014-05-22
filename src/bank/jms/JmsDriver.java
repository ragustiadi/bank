package bank.jms;

import java.io.IOException;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.commands.*;

public class JmsDriver implements bank.BankDriver2 {
	
	private bank.Bank bank;
	JMSProducer operations;
	JMSConsumer updates;
	Queue queue;
	Topic topic;

	@Override
	public void connect(String[] args) throws IOException {
		try {
			final Context jndiContext = new InitialContext();
			final ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
			queue = (Queue) jndiContext.lookup("BANK");
			topic = (Topic) jndiContext.lookup("BANK.LISTENER");
			
			updates = factory.createContext().createConsumer(topic);
			operations = factory.createContext().createProducer();
			
		} catch (NamingException e) {
			System.out.println("Cannot bind JMS-resources.");
		}
		
		bank = new JmsBank();
	}

	@Override
	public void disconnect() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	@Override
	public void registerUpdateHandler(UpdateHandler handler) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	public class JmsBank implements bank.Bank {

		@Override
		public String createAccount(String owner) throws IOException {
			operations.send(queue, new CreateAccount(owner));
			Message response = updates.receive(1000);
			if (response == null) System.err.println("Create account failed.");
			String ret = null;
			try {
				ret = response.getStringProperty("number");
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ret;
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			operations.setJMSType("GetAccountNumbers");
			operations.send(queue, new GetAccountNumbers());
			Message response = updates.receive(1000);
			if (response == null) System.err.println("Get accounts failed.");
			Set<String> ret = null;
			try {
				ret = (Set) response.getObjectProperty("accounts");
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ret;
		}

		@Override
		public Account getAccount(String number) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void transfer(Account a, Account b, double amount) throws IOException,
				IllegalArgumentException, OverdrawException, InactiveException {
			// TODO Auto-generated method stub
			
		}
		
	}

}
