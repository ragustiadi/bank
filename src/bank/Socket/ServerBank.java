package bank.Socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

public class ServerBank implements Bank {

	private static final Map<String, ServerAccount> accounts = new HashMap<String, ServerAccount>();

	public String createAccount(String owner) throws IOException {
		String number = getNewAccNumber();
		accounts.put(number, new ServerAccount(number, owner));
		return number;
	}

	public boolean closeAccount(String number) throws IOException {
		if (accounts.containsKey(number) && accounts.get(number).isActive()
				&& accounts.get(number).getBalance() == 0) {
			accounts.get(number).active = false;
			return true;
		}
		return false;
	}

	public Set getAccountNumbers() throws IOException {
		Set<String> numbers = new HashSet<String>();
		for (String s : accounts.keySet()) {
			if (accounts.get(s).isActive())
				numbers.add(s);
		}
		return numbers;
	}

	public bank.Account getAccount(String number) throws IOException {
		return accounts.get(number);
	}

	public void transfer(bank.Account a, bank.Account b, double amount)
			throws IOException, IllegalArgumentException, OverdrawException,
			InactiveException {
		if (a != null && b != null) {
			a.withdraw(amount);
			b.deposit(amount);
		} else {
			throw new IOException("The account does not exist.");
		}

	}

	private String getNewAccNumber() {
		String number = Long.toHexString(System.currentTimeMillis())
				.toUpperCase();
		while (accounts.containsKey(number))
			number = Long.toHexString(System.currentTimeMillis()).toUpperCase();
		return number;
	}

	class ServerAccount implements bank.Account {

		private String number;
		private String owner;
		private double balance;
		private boolean active = true;

		public ServerAccount(String num, String own) {
			number = num;
			owner = own;
		}

		@Override
		public String getNumber() throws IOException {
			return number;
		}

		@Override
		public String getOwner() throws IOException {
			return owner;
		}

		@Override
		public boolean isActive() throws IOException {
			return active;
		}

		@Override
		public void deposit(double amount) throws IOException,
				IllegalArgumentException, InactiveException {
			if (active) {
				if (amount >= 0)
					balance += amount;
				else
					throw new IllegalArgumentException("Amount is neagtive.");
			} else {
				throw new InactiveException("Account not active.");
			}
		}

		@Override
		public void withdraw(double amount) throws IOException,
				IllegalArgumentException, OverdrawException, InactiveException {
			if (active) {
				if (amount >= 0) {
					if (balance >= amount)
						balance -= amount;
					else
						throw new OverdrawException("Not sufficient funds.");
				} else {
					throw new IllegalArgumentException("Amount is negative.");
				}
			} else {
				throw new InactiveException("Account not active.");
			}
		}

		@Override
		public double getBalance() throws IOException {
			return balance;
		}

	}
}
