package bank.commands;

import java.io.IOException;
import java.io.Serializable;

import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

public class Transfer implements Command, Serializable {

	private String fromNumber;
	private String toNumber;
	private double amount;

	public Transfer(String from, String to, double a) {
		fromNumber = from;
		toNumber = to;
		amount = a;
	}

	@Override
	public Object execute(Bank bank) throws IOException {
		try {
			bank.transfer(bank.getAccount(fromNumber), bank.getAccount(toNumber), amount);
		} catch (IllegalArgumentException | OverdrawException | InactiveException e) {
			return e;
		}
		return null;
	}
}
