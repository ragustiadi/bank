package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class GetAccount implements Command, Serializable {

	private String number = null;

	public GetAccount(String n) {
		number = n;
	}

	public void execute(bank.Bank bank, ObjectOutputStream dataOut)
			throws IOException {
		try {
			bank.Account acc = bank.getAccount(number);
			if (acc == null)
				dataOut.writeObject(null);
			else
				dataOut.writeObject(acc.getNumber());
		} catch (IOException e) {
			dataOut.writeObject(e);
		}
	}
}
