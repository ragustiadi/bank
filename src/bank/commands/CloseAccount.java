package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bank.Bank;

public class CloseAccount implements Command, Serializable {

	private String number;

	public CloseAccount(String num) {
		number = num;
	}

	@Override
	public void execute(Bank bank, ObjectOutputStream dataOut)
			throws IOException {
		boolean success = bank.closeAccount(number);
		try {
			dataOut.writeObject(success);
		} catch (Exception e) {
			dataOut.writeObject(e);
		}
	}
}
