package bank.jms;

import java.io.IOException;
import java.util.Set;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
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
	private JMSProducer operations;
	private JMSConsumer updates;
	private Queue queue;
	private Topic topic;
	private UpdateHandler handler;

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
	public void disconnect() throws IOException { }

	@Override
	public Bank getBank() {
		return bank;
	}

	@Override
	public void registerUpdateHandler(UpdateHandler handler) throws IOException {
		this.handler = handler;
		System.out.println("Hash code set: " + handler.hashCode());
		operations.setJMSCorrelationID(Integer.toHexString(this.handler.hashCode()));
	}
	
	public class JmsBank implements bank.Bank {

		@Override
		public String createAccount(String owner) throws IOException {
			System.out.println("create for " + owner);
			operations.send(queue, new CreateAccount(owner));
			String n = (String) getResponse();
			System.out.println("number for new account is " + n);
			return n;

		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			System.out.println("close account " + number);
			operations.send(queue, new CloseAccount(number));
			return (boolean) getResponse();
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			System.out.println("get all account numbers");
			operations.send(queue, new GetAccountNumbers());
			Set<String> resp = (Set<String>) getResponse();
			System.out.println(resp);
			return resp;
		}

		@Override
		public Account getAccount(String number) throws IOException {
			System.out.println("get account " + number);
			operations.send(queue, new GetAccount(number));
			String acc = (String) getResponse();
			if (acc == null)
				return null;
			else
				return new JmsAccount(acc);
		}

		@Override
		public void transfer(Account a, Account b, double amount)
				throws IOException, IllegalArgumentException, OverdrawException, InactiveException {
			System.out.println("transfer from " + a.getNumber() + " to " + b.getNumber() + " " + amount);
			operations.send(queue, new Transfer(a.getNumber(), b.getNumber(), amount));
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
			ObjectMessage message = (ObjectMessage) updates.receive();
			Object ret = null;
			try {
				ret = message.getObject();
			} catch (JMSException e) {
				System.out.println("No valid object.");
			}
			System.out.println(ret);
			return ret;
		}
		
		class JmsAccount implements bank.Account {
			
			private String number;
			
			public JmsAccount(String num) {
				number = num;
			}

			@Override
			public String getNumber() throws IOException {
				System.out.println("get number for " + number);
				return number;
			}

			@Override
			public String getOwner() throws IOException {
				System.out.println("get owner for " + number);
				operations.send(queue, new AccountGetOwner(number));
				return (String) getResponse();
			}

			@Override
			public boolean isActive() throws IOException {
				System.out.println("is " + number + " active");
				operations.send(queue, new AccountIsActive(number));
				return (boolean) getResponse();
			}

			@Override
			public void deposit(double amount) throws IOException, IllegalArgumentException, 
					InactiveException {
				System.out.println("deposit to " + number + " " + amount);
				operations.send(queue, new AccountDeposit(number, amount));
				Object o = getResponse();
				if (o != null) {
					if (o instanceof IllegalArgumentException)
						throw new IllegalArgumentException();
					else if (o instanceof InactiveException)
						throw new InactiveException();
				}
			}

			@Override
			public void withdraw(double amount) throws IOException, IllegalArgumentException,
					OverdrawException, InactiveException {
				System.out.println("withdraw from " + number + " " + amount);
				operations.send(queue, new AccountWithdraw(number, amount));
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
				System.out.println("get balance of " + number);
				operations.send(queue, new AccountGetBalance(number));
				return (double) getResponse();
			}
		}
	}

}
