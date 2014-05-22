package bank.commands;

import java.io.IOException;
import java.io.Serializable;

import bank.Bank;

public class AccountGetOwner implements Command, Serializable {

	private String number;

	public AccountGetOwner(String num) {
		number = num;
	}

	@Override
	public Object execute(Bank bank) throws IOException {
		String owner = bank.getAccount(number).getOwner();
		return owner;
	}
}
