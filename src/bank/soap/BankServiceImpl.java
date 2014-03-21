package bank.soap;

import java.io.IOException;
import java.util.Set;

import javax.jws.WebService;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;

@WebService
public class BankServiceImpl implements BankService {

	@Override
	public String createAccount(String owner) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean closeAccount(String number) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<String> getAccountNumbers() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account getAccount(String number) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void transfer(Account a, Account b, double amount)
			throws IOException, IllegalArgumentException, OverdrawException,
			InactiveException {
		// TODO Auto-generated method stub

	}

	@WebService
	public class AccountServiceImpl implements AccountService {

		@Override
		public String getNumber() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getOwner() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isActive() throws IOException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void deposit(double amount) throws IOException,
				IllegalArgumentException, InactiveException {
			// TODO Auto-generated method stub

		}

		@Override
		public void withdraw(double amount) throws IOException,
				IllegalArgumentException, OverdrawException, InactiveException {
			// TODO Auto-generated method stub

		}

		@Override
		public double getBalance() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

	}

}
