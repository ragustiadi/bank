package bank.rmi;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.EventListener;
import java.util.Set;

import bank.Account;
import bank.Bank;
import bank.BankDriver2;
import bank.InactiveException;
import bank.OverdrawException;

public class RmiDriver implements BankDriver2, EventListener {

	RemoteBank bank = null;

	RmiUpdateHandler handler = null;

	@Override
	public void connect(String[] args) throws IOException {
		try {
			bank = (RemoteBank) Naming.lookup("rmi://localhost/BankService");
		} catch (NotBoundException e) {
			System.err.println("Connection not found.");
		}
	}

	@Override
	public void disconnect() throws IOException {
		bank.removeUpdateHandler(handler);
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	@Override
	public void registerUpdateHandler(UpdateHandler handler) throws IOException {
		this.handler = new RmiUpdateHandlerImpl(handler);
		bank.addUpdateHandler(this.handler);
	}

	public class RmiBank implements bank.Bank {

		@Override
		public String createAccount(String owner) throws IOException {
			String number = bank.createAccount(owner);
			System.out.println("Account #" + number + " created.");
			return number;
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			boolean closed = bank.closeAccount(number);
			System.out.println("Account #" + number + " created.");
			return closed;
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			return bank.getAccountNumbers();
		}

		@Override
		public Account getAccount(String number) throws IOException {
			return bank.getAccount(number);
		}

		@Override
		public void transfer(Account a, Account b, double amount) throws IOException,
				IllegalArgumentException, OverdrawException, InactiveException {
			transfer(a, b, amount);
		}
	}
}
