package bank.jms;

import java.io.IOException;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TemporaryQueue;
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
	private JMSContext context;
	private JMSProducer operationsProducer;
	private JMSConsumer updatesConsumer;
	private JMSConsumer responseConsumer;
	private Queue queue;
	private Topic topic;
	private TemporaryQueue response;

	@Override
	public void connect(String[] args) throws IOException {
		try {
			final Context jndiContext = new InitialContext();
			final ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
			context = factory.createContext();
						
			queue = (Queue) jndiContext.lookup("BANK");
			topic = (Topic) jndiContext.lookup("BANK.LISTENER");
			response = context.createTemporaryQueue();
			
			updatesConsumer = context.createConsumer(topic);
			responseConsumer = context.createConsumer(response);
			operationsProducer = context.createProducer().setJMSReplyTo(response);
			
			System.out.println("Client initialized.");
		} catch (NamingException e) {
			System.err.println("Cannot bind JMS-resources.");
		}
		bank = new JmsBank();
	}

	@Override
	public void disconnect() throws IOException { }

	@Override
	public Bank getBank() {
		return bank;
	}

	@Override
	public void registerUpdateHandler(UpdateHandler handler) throws IOException {
		updatesConsumer.setMessageListener(new UpdateListener(handler));
	}
	
	public class JmsBank implements bank.Bank {

		@Override
		public String createAccount(String owner) throws IOException {
			operationsProducer.send(queue, new CreateAccount(owner));
			return (String) getResponse();
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			operationsProducer.send(queue, new CloseAccount(number));
			return (boolean) getResponse();
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			operationsProducer.send(queue, new GetAccountNumbers());
			return (Set<String>) getResponse();
		}

		@Override
		public Account getAccount(String number) throws IOException {
			operationsProducer.send(queue, new GetAccount(number));
			String acc = (String) getResponse();
			if (acc == null)
				return null;
			else
				return new JmsAccount(acc);
		}

		@Override
		public void transfer(Account a, Account b, double amount)
				throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
			operationsProducer.send(queue, new Transfer(a.getNumber(), b.getNumber(), amount));
			Object o = getResponse();
			if (o != null)
				if (o instanceof IOException)
					throw new IOException();
				else if (o instanceof IllegalArgumentException)
					throw new IllegalArgumentException();
				else if (o instanceof OverdrawException)
					throw new OverdrawException();
				else if (o instanceof InactiveException)
					throw new InactiveException();
		}
		
		private Object getResponse() {
			Message message = responseConsumer.receive(5000);	// 5s timeout
			if (message == null) 
				System.err.println("Response timed out.");
			Object ret = null;
			try {
				ret = ((ObjectMessage)message).getObject();
			} catch (JMSException e) {
				System.err.println("Not a valid object.");
			}
			return ret;
		}
		
		class JmsAccount implements bank.Account {
			
			private String number;
			
			public JmsAccount(String num) {
				number = num;
			}

			@Override
			public String getNumber() throws IOException {
				return number;
			}

			@Override
			public String getOwner() throws IOException {
				operationsProducer.send(queue, new AccountGetOwner(number));
				return (String) getResponse();
			}

			@Override
			public boolean isActive() throws IOException {
				operationsProducer.send(queue, new AccountIsActive(number));
				return (boolean) getResponse();
			}

			@Override
			public void deposit(double amount)
					throws IOException, IllegalArgumentException, InactiveException {
				operationsProducer.send(queue, new AccountDeposit(number, amount));
				Object o = getResponse();
				if (o != null) {
					if (o instanceof IllegalArgumentException)
						throw new IllegalArgumentException();
					else if (o instanceof InactiveException)
						throw new InactiveException();
				}
			}

			@Override
			public void withdraw(double amount)
					throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
				operationsProducer.send(queue, new AccountWithdraw(number, amount));
				Object o = getResponse();
				if (o != null) {
					if (o instanceof IllegalArgumentException)
						throw new IllegalArgumentException();
					else if (o instanceof InactiveException)
						throw new InactiveException();
					else if (o instanceof OverdrawException)
						throw new OverdrawException();
				}
			}

			@Override
			public double getBalance() throws IOException {
				operationsProducer.send(queue, new AccountGetBalance(number));
				return (double) getResponse();
			}
		}
	}
}
