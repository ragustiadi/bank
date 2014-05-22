package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bank.Bank;
import bank.InactiveException;

public class AccountDeposit implements Command, Serializable {

	private String number;
	private double amount;

	public AccountDeposit(String num, double am) {
		number = num;
		amount = am;
	}

	@Override
	public Object execute(Bank bank) throws IOException {
		try {
			bank.getAccount(number).deposit(amount);
		} catch (InactiveException | IllegalArgumentException e) {
			return e;
		}
		return null;
	}
}
