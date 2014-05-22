package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class GetAccount implements Command, Serializable {

	private String number = null;

	public GetAccount(String n) {
		number = n;
	}

	public Object execute(bank.Bank bank) throws IOException {
			bank.Account acc = bank.getAccount(number);
			if (acc == null)
				return null;
			else
				return acc.getNumber();
	}
}
