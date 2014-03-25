package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

import bank.Bank;

public class GetAccountNumbers implements Command, Serializable {

	@Override
	public void execute(Bank bank, ObjectOutputStream dataOut)
			throws IOException {
		Set<String> accounts = bank.getAccountNumbers();
		try {
			dataOut.writeObject(accounts);
		} catch (Exception e) {
			dataOut.writeObject(e);
		}
	}
}
