package bank.websockets;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.Session;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;
import bank.BankDriver2.UpdateHandler;
import bank.commands.AccountDeposit;
import bank.commands.AccountGetBalance;
import bank.commands.AccountGetOwner;
import bank.commands.AccountIsActive;
import bank.commands.AccountWithdraw;
import bank.commands.CloseAccount;
import bank.commands.CreateAccount;
import bank.commands.GetAccount;
import bank.commands.GetAccountNumbers;
import bank.commands.Transfer;


@ClientEndpoint(encoders = CommandEncoder.class, decoders = CommandDecoder.class)
public class WebsocketsClientBank implements bank.Bank {
		
		private Session session;
		private BlockingQueue<Object> responses = new ArrayBlockingQueue<Object>(1);
		private UpdateHandler handler;
				
		public WebsocketsClientBank() { }
		
		public WebsocketsClientBank(Session s) {
			session = s;
		}
		
		public void setSession(Session s) {
			session = s;
		}
		
		public Session getSession() {
			return session;
		}
		
		@OnMessage
		public void onMessage(Object message) {
			WebsocketsResponse response = (WebsocketsResponse) message;
			try {
				if (response.isNotification())
					handler.accountChanged((String) response.getObject());
				else
					responses.put(response.getObject());
			} catch (IOException | InterruptedException e) {
				System.out.println("Queue full. Could not insert message.");
			}
		}
		
		public void setUpdateHandler(UpdateHandler updateHandler) {
			handler = updateHandler;
		}

		private Object send(Object cmd) {
			try {
				System.out.println("session size: " + session.getOpenSessions().size());
				session.getBasicRemote().sendObject(cmd);
			} catch (IOException | EncodeException e) {
				System.err.println("could not send command");
			}
			Object ret = null;
			try {
				ret = responses.poll(1000, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				System.err.println("timeout reached");
			}
			return ret;
		}

		@Override
		public String createAccount(String owner) throws IOException {
			Object response = send(new CreateAccount(owner));
			return (String) response;
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			Object response = send(new CloseAccount(number));
			return (boolean) response;
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			Object response = send(new GetAccountNumbers());
			if (response instanceof IOException)
				throw (IOException) response;
			if (response instanceof Set)
				return (Set<String>) response;
			else
				return null;
		}

		@Override
		public Account getAccount(String number) throws IOException {
			Object response = send(new GetAccount(number));
			if (response == null)
				return null;
			if (response instanceof IOException)
				throw (IOException) response;
			else
				return new WebsocketsAccount((String) response);
		}

		@Override
		public void transfer(Account a, Account b, double amount) throws IOException,
				IllegalArgumentException, OverdrawException, InactiveException {
			Object response = send(new Transfer(a.getNumber(), b.getNumber(), amount));
			if (response instanceof IOException)
				throw (IOException) response;
			else if (response instanceof IllegalArgumentException)
				throw (IllegalArgumentException) response;
			else if (response instanceof OverdrawException)
				throw (OverdrawException) response;
			else if (response instanceof InactiveException)
				throw (InactiveException) response;
		}
		
		class WebsocketsAccount implements Account {
			
			private String number;
			
			public WebsocketsAccount(String n) {
				number = n;
			}

			@Override
			public String getNumber() throws IOException {
				return number;
			}

			@Override
			public String getOwner() throws IOException {
				Object response = send(new AccountGetOwner(number));
				if (response instanceof IOException)
					throw (IOException) response;
				return (String) response;
			}

			@Override
			public boolean isActive() throws IOException {
				Object response = send(new AccountIsActive(number));
				if (response instanceof IOException)
					throw (IOException) response;
				return (boolean) response;
			}

			@Override
			public void deposit(double amount) throws IOException, IllegalArgumentException,
					InactiveException {
				Object response = send(new AccountDeposit(number, amount));
				if (response instanceof IOException)
					throw (IOException) response;
				else if (response instanceof IllegalAccessException)
					throw (IllegalArgumentException) response;
				else if (response instanceof InactiveException)
					throw (InactiveException) response;
			}

			@Override
			public void withdraw(double amount) throws IOException, IllegalArgumentException,
					OverdrawException, InactiveException {
				Object response = send(new AccountWithdraw(number, amount));
				if (response instanceof IOException)
					throw (IOException) response;
				else if (response instanceof IllegalArgumentException)
					throw (IllegalArgumentException) response;
				else if (response instanceof OverdrawException)
					throw (OverdrawException) response;
				else if (response instanceof InactiveException)
					throw (InactiveException) response;
			}

			@Override
			public double getBalance() throws IOException {
				Object response = send(new AccountGetBalance(number));
				if (response instanceof IOException)
					throw (IOException) response;
				return (double) response;
			}
			
		}
	}