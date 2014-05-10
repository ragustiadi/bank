package bank.rmi;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import bank.Account;
import bank.Bank;
import bank.BankDriver2;
import bank.InactiveException;
import bank.OverdrawException;

public class RmiDriver implements BankDriver2 {

	Bank bank = null;
	List<UpdateHandler> update = new LinkedList<UpdateHandler>();

	@Override
	public void connect(String[] args) throws IOException {
		try {
			bank = (Bank) Naming.lookup("rmi://localhost/BankService");
		} catch (NotBoundException e) {
			System.out.println("Connection not found.");
			e.printStackTrace();
		}
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
		update.add(handler);
	}

	public class RmiBank implements bank.Bank {

		@Override
		public String createAccount(String owner) throws IOException {
			String number = bank.createAccount(owner);
			System.out.println("Account #" + number + " created.");
			notify(number);
			return number;
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			boolean closed = bank.closeAccount(number);
			System.out.println("Account #" + number + " created.");
			notify(number);
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
			notify(a.getNumber());
			notify(b.getNumber());
		}

		private void notify(String number) {
			for (UpdateHandler handler : update) {
				try {
					handler.accountChanged(number);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
