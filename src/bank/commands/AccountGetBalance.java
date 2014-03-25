package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bank.Bank;

public class AccountGetBalance implements Command, Serializable {

	private static final long serialVersionUID = 922317277434652669L;
	private String number;

	public AccountGetBalance(String num) {
		number = num;
	}

	@Override
	public void execute(Bank bank, ObjectOutputStream dataOut)
			throws IOException {
		double balance = bank.getAccount(number).getBalance();
		try {
			dataOut.writeObject(balance);
		} catch (Exception e) {
			dataOut.writeObject(e);
		}
	}
}
