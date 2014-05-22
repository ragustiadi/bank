package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bank.Bank;

public class CreateAccount implements Command, Serializable {

	private String owner = null;

	public CreateAccount(String o) {
		owner = o;
	}

	public Object execute(Bank bank) throws IOException {
		String number = bank.createAccount(owner);
		return number;
	}
}
