/*
 * Copyright (c) 2000-2014 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.local;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bank.InactiveException;
import bank.OverdrawException;

public class Driver implements bank.BankDriver {
	private Bank bank = null;

	@Override
	public void connect(String[] args) {
		bank = new Bank();
		System.out.println("connected...");
	}

	@Override
	public void disconnect() {
		bank = null;
		System.out.println("disconnected...");
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	static class Bank implements bank.Bank {

		private final Map<String, Account> accounts = new HashMap<>();

		@Override
		public Set<String> getAccountNumbers() {
			Set<String> actAcc = new HashSet<String>();
			for (String s : accounts.keySet()) {
				if (accounts.get(s).isActive())
					actAcc.add(s);
			}
			return actAcc;
		}

		@Override
		public String createAccount(String owner) {
			String number = getNewAccNumber();
			accounts.put(number, new Account(number, owner));
			System.out.println(number);
			return number;
		}

		@Override
		public boolean closeAccount(String number) {
			if (accounts.containsKey(number) && accounts.get(number).isActive()
					&& accounts.get(number).getBalance() == 0) {
				accounts.get(number).active = false;
				return true;
			}
			return false;
		}

		@Override
		public bank.Account getAccount(String number) {
			return accounts.get(number);
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException {
			if (from != null && to != null) {
				from.withdraw(amount);
				to.deposit(amount);
			} else
				throw new IOException("The account does not exist.");
		}

		private String getNewAccNumber() {
			String ret = Long.toHexString(System.currentTimeMillis())
					.toUpperCase();
			while (accounts.containsKey(ret))
				ret = Long.toHexString(System.currentTimeMillis())
						.toUpperCase();
			return ret;
		}
	}

	static class Account implements bank.Account {
		private String number;
		private String owner;
		private double balance;
		private boolean active = true;

		Account(String number, String owner) {
			this.owner = owner;
			this.number = number;
		}

		@Override
		public double getBalance() {
			return balance;
		}

		@Override
		public String getOwner() {
			return owner;
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public boolean isActive() {
			return active;
		}

		@Override
		public void deposit(double amount) throws InactiveException {
			if (active)
				if (amount >= 0)
					balance += amount;
				else
					throw new IllegalArgumentException("Amount is negative.");
			else
				throw new InactiveException("Account not active.");
		}

		@Override
		public void withdraw(double amount) throws InactiveException,
				OverdrawException {
			if (amount < 0)
				throw new IllegalArgumentException("Amount is negative.");
			if (active)
				if (balance >= amount)
					balance -= amount;
				else
					throw new OverdrawException("Not sufficient funds.");
			else
				throw new InactiveException("Account not active.");
		}

	}

}