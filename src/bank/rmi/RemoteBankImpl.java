package bank.rmi;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

public class RemoteBankImpl extends UnicastRemoteObject implements RemoteBank {

	bank.Bank bank = null;

	public RemoteBankImpl(Bank bank) throws RemoteException {
		super(8888);
		this.bank = bank;
	}

	@Override
	public String createAccount(String owner) throws IOException {
		System.out.println("Create account for " + owner);
		return bank.createAccount(owner);
	}

	@Override
	public boolean closeAccount(String number) throws IOException {
		System.out.println("Close account #" + number);
		return bank.closeAccount(number);
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
		System.out.println("Transfer " + amount + " from " + a + " to " + b);
		bank.transfer(a, b, amount);
	}

	public class RemoteAccountImpl extends UnicastRemoteObject implements RemoteAccount {

		Account account = null;

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
		}

		@Override
		public void withdraw(double amount) throws IOException, IllegalArgumentException,
				OverdrawException, InactiveException {
			account.withdraw(amount);
		}

		@Override
		public double getBalance() throws IOException {
			return account.getBalance();
		}

	}

}
