package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bank.Bank;

public class AccountIsActive implements Command, Serializable {

	private String number;

	public AccountIsActive(String num) {
		number = num;
	}

	@Override
	public Object execute(Bank bank) throws IOException {
		boolean active = bank.getAccount(number).isActive();
		return active;
	}
}
