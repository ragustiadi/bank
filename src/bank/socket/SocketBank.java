package bank.socket;

import java.io.IOException;
import java.util.Set;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;
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

/*
 * Schnittstelle den SocketDriver um mit dem Server zu kommunizieren. Die
 * SocketBank speichert selber keine Daten, sondern sendet Anfragen (Commands)
 * an den Server.
 */
public class SocketBank implements bank.Bank {

	private SocketDriver driver = null;

	public SocketBank(SocketDriver d) {
		driver = d;
	}

	@Override
	public String createAccount(String owner) throws IOException {
		driver.dataOut.writeObject(new CreateAccount(owner));
		String response = null;
		try {
			response = (String) driver.dataIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public boolean closeAccount(String number) throws IOException {
		driver.dataOut.writeObject(new CloseAccount(number));
		boolean success = false;
		try {
			success = (boolean) driver.dataIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public Set getAccountNumbers() throws IOException {
		driver.dataOut.writeObject(new GetAccountNumbers());
		Set accounts = null;
		try {
			accounts = (Set) driver.dataIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	@Override
	public Account getAccount(String number) throws IOException {
		driver.dataOut.writeObject(new GetAccount(number));
		String account = null;
		try {
			account = (String) driver.dataIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (account == null)
			return null;
		else
			return new LocalAccount(account);
	}

	@Override
	public void transfer(bank.Account a, bank.Account b, double amount)
			throws IOException, IllegalArgumentException, OverdrawException,
			InactiveException {
		driver.dataOut.writeObject(new Transfer(a.getNumber(), b.getNumber(),
				amount));
		Exception exc = null;
		try {
			exc = (Exception) driver.dataIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (exc instanceof IllegalArgumentException)
			throw (IllegalArgumentException) exc;
		else if (exc instanceof InactiveException)
			throw (InactiveException) exc;
		else if (exc instanceof IOException)
			throw (IOException) exc;
		else if (exc instanceof OverdrawException)
			throw (OverdrawException) exc;

	}

	/*
	 * LocalAccount ist ein DummyAccount, der nur die Nummer kennt. Die
	 * restlichen Daten werden vom Server angefragt. Die Nummer dient nur der
	 * eindeutigen Indentifikation des Accounts auf dem Server.
	 */
	class LocalAccount implements bank.Account {

		private String number;

		public LocalAccount(String num) {
			number = num;
		}

		@Override
		public String getNumber() throws IOException {
			return number;
		}

		@Override
		public String getOwner() throws IOException {
			driver.dataOut.writeObject(new AccountGetOwner(number));
			String owner = null;
			try {
				owner = (String) driver.dataIn.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return owner;
		}

		@Override
		public boolean isActive() throws IOException {
			driver.dataOut.writeObject(new AccountIsActive(number));
			boolean active = false;
			try {
				active = (boolean) driver.dataIn.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return active;
		}

		@Override
		public void deposit(double amount) throws IOException,
				IllegalArgumentException, InactiveException {
			driver.dataOut.writeObject(new AccountDeposit(number, amount));
			Exception exc = null;
			try {
				exc = (Exception) driver.dataIn.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if (exc instanceof IllegalArgumentException)
				throw (IllegalArgumentException) exc;
			else if (exc instanceof InactiveException)
				throw (InactiveException) exc;
			else if (exc instanceof IOException)
				throw (IOException) exc;
		}

		@Override
		public void withdraw(double amount) throws IOException,
				IllegalArgumentException, OverdrawException, InactiveException {
			driver.dataOut.writeObject(new AccountWithdraw(number, amount));
			try {
				driver.dataIn.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		@Override
		public double getBalance() throws IOException {
			driver.dataOut.writeObject(new AccountGetBalance(number));
			double balance = Double.NaN;
			try {
				balance = (double) driver.dataIn.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return balance;
		}
	}

}
