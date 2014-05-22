package bank.commands;

import java.io.IOException;
import java.io.Serializable;

import bank.Bank;

public class CloseAccount implements Command, Serializable {

	private String number;

	public CloseAccount(String num) {
		number = num;
	}

	@Override
	public Object execute(Bank bank) throws IOException {
		boolean success = bank.closeAccount(number);
		return success;
	}
}
