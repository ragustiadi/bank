package bank.rmi;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import bank.Account;
import bank.Bank;
import bank.BankDriver2.UpdateHandler;
import bank.InactiveException;
import bank.OverdrawException;

public class RemoteBankImpl extends UnicastRemoteObject implements RemoteBank {

	private bank.Bank bank = null;
	private List<UpdateHandler> clients = new LinkedList<UpdateHandler>();

	public RemoteBankImpl(Bank bank) throws RemoteException {
		super(8888);
		this.bank = bank;
	}

	public void addUpdateHandler(RmiUpdateHandler client) {
		boolean success = clients.add(client);
		if (!success)
			System.err.println("Client already in list");
		else
			System.out.println("New client connected");
	}

	public void removeUpdateHandler(RmiUpdateHandler client) {
		boolean success = clients.remove(client);
		if (!success)
			System.err.println("Client could not be found");
		else
			System.out.println("Client logged off");
	}

	private void notifyClients(String number) throws IOException {
		for (UpdateHandler client : clients)
			client.accountChanged(number);
		System.out.println("Clients notified");
	}

	@Override
	public String createAccount(String owner) throws IOException {
		System.out.println("Create account for " + owner);
		String accNumber = bank.createAccount(owner);
		notifyClients(accNumber);
		return accNumber;
	}

	@Override
	public boolean closeAccount(String number) throws IOException {
		System.out.println("Close account #" + number);
		boolean success = bank.closeAccount(number);
		if (success)
			notifyClients(number);
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
		if (ret != null)
			return new RemoteAccountImpl(bank.getAccount(number));
		else
			return null;
	}

	@Override
	public void transfer(Account a, Account b, double amount) throws IOException,
			IllegalArgumentException, OverdrawException, InactiveException {
		System.out
				.println("Transfer " + amount + " from " + a.getNumber() + " to " + b.getNumber());
		bank.transfer(a, b, amount);
	}

	public class RemoteAccountImpl extends UnicastRemoteObject implements RemoteAccount {

		private Account account = null;

		public RemoteAccountImpl(Account acc) throws RemoteException {
			super(8888);
			account = acc;
		}

		@Override
		public String getNumber() throws IOException {
			return account.getNumber();
		}

		@Override
		public String getOwner() throws IOException {
			return account.getOwner();
		}

		@Override
		public boolean isActive() throws IOException {
			return account.isActive();
		}

		@Override
		public void deposit(double amount) throws IOException, IllegalArgumentException,
				InactiveException {
			account.deposit(amount);
			notifyClients(account.getNumber());
		}

		@Override
		public void withdraw(double amount) throws IOException, IllegalArgumentException,
				OverdrawException, InactiveException {
			account.withdraw(amount);
			notifyClients(account.getNumber());
		}

		@Override
		public double getBalance() throws IOException {
			return account.getBalance();
		}
	}
}
