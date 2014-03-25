package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bank.Bank;

public class AccountDeposit implements Command, Serializable {

	private String number;
	private double amount;

	public AccountDeposit(String num, double am) {
		number = num;
		amount = am;
	}

	@Override
	public void execute(Bank bank, ObjectOutputStream dataOut)
			throws IOException {
		try {
			bank.getAccount(number).deposit(amount);
			dataOut.writeObject(null);
		} catch (Exception e) {
			dataOut.writeObject(e);
		}
	}
}
