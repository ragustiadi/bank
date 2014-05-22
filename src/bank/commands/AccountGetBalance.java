package bank.commands;

import java.io.IOException;
import java.io.Serializable;

import bank.Bank;

public class AccountGetBalance implements Command, Serializable {

	private String number;

	public AccountGetBalance(String num) {
		number = num;
	}

	@Override
	public Object execute(Bank bank) throws IOException {
		double balance = bank.getAccount(number).getBalance();
		return balance;
	}
}
