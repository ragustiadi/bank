package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bank.Bank;

public class AccountDeposit implements Command, Serializable {

	private static final long serialVersionUID = -2601470840227021751L;
	private String number;
	private double amount;

	public AccountDeposit(String num, double am) {
		number = num;
		amount = am;
	}

	@Override
	public synchronized void execute(Bank bank, ObjectOutputStream dataOut)
			throws IOException {
		try {
			bank.getAccount(number).deposit(amount);
			dataOut.writeObject(null);
		} catch (Exception e) {
			dataOut.writeObject(e);
		}
	}
}
