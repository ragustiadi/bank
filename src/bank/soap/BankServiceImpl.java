package bank.soap;

import java.io.IOException;

import javax.jws.WebParam;
import javax.jws.WebService;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;

@WebService
public class BankServiceImpl implements BankService {

	private static bank.Bank bank = new bank.common.ServerBank();

	@Override
	public String createAccount(@WebParam(name = "owner") String owner)
			throws IOException {
		return bank.createAccount(owner);
	}

	@Override
	public boolean closeAccount(@WebParam(name = "number") String number)
			throws IOException {
		return bank.closeAccount(number);
	}

	@Override
	public Object[] getAccountNumbers() throws IOException {
		return bank.getAccountNumbers().toArray();
	}

	@Override
	public String getAccount(@WebParam(name = "number") String number)
			throws IOException {
		Account temp = bank.getAccount(number);
		if (temp == null)
			return "";
		else
			return temp.getNumber();
	}

	@Override
	public void transfer(@WebParam(name = "a") String a,
			@WebParam(name = "b") String b,
			@WebParam(name = "amount") double amount) throws IOException,
			IllegalArgumentException, OverdrawException, InactiveException,
			CustomException {
		try {
			bank.transfer(bank.getAccount(a), bank.getAccount(b), amount);
		} catch (IllegalArgumentException e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public String getOwner(@WebParam(name = "number") String number)
			throws IOException {
		return bank.getAccount(number).getOwner();
	}

	@Override
	public boolean isActive(@WebParam(name = "number") String number)
			throws IOException {
		return bank.getAccount(number).isActive();
	}

	@Override
	public void deposit(@WebParam(name = "number") String number,
			@WebParam(name = "amount") double amount) throws IOException,
			IllegalArgumentException, InactiveException, CustomException {
		try {
			bank.getAccount(number).deposit(amount);
		} catch (IllegalArgumentException e) {
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public void withdraw(@WebParam(name = "number") String number,
			@WebParam(name = "amount") double amount) throws IOException,
			IllegalArgumentException, OverdrawException, InactiveException,
			CustomException {
		try {
			bank.getAccount(number).withdraw(amount);
		} catch (IllegalArgumentException e) {
			throw new CustomException(e.getMessage());
		}

	}

	@Override
	public double getBalance(@WebParam(name = "number") String number)
			throws IOException {
		return bank.getAccount(number).getBalance();
	}

	class CustomException extends Exception {
		// Markierexception für IllegalargumentException
		public CustomException(String message) {
			super(message);
		}
	}
}
