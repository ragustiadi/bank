package bank.commands;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import bank.Bank;

public class GetAccountNumbers implements Command, Serializable {

	@Override
	public Object execute(Bank bank) throws IOException {
		Set<String> accounts = bank.getAccountNumbers();
		return accounts;
	}
}
