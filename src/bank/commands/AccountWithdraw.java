package bank.commands;

import java.io.IOException;
import java.io.Serializable;

import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

public class AccountWithdraw implements Command, Serializable {

	private String number;
	private double amount;

	public AccountWithdraw(String num, double am) {
		number = num;
		amount = am;
	}

	@Override
	public Object execute(Bank bank) throws IOException {
		try {
			bank.getAccount(number).withdraw(amount);
		} catch (OverdrawException | InactiveException | IllegalArgumentException e) {
			return e;
		}
		return null;
	}
}
