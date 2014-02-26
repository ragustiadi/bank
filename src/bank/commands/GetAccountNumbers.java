package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

import bank.Bank;

public class GetAccountNumbers implements Command, Serializable {

	private static final long serialVersionUID = -1409329087260599897L;

	@Override
	public synchronized void execute(Bank bank, ObjectOutputStream dataOut)
			throws IOException {
		Set<String> accounts = bank.getAccountNumbers();
		try {
			dataOut.writeObject(accounts);
		} catch (Exception e) {
			dataOut.writeObject(e);
		}
	}
}
