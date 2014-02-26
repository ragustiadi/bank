package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bank.Bank;

public class AccountGetOwner implements Command, Serializable {

	private static final long serialVersionUID = 1987643040240267322L;
	private String number;

	public AccountGetOwner(String num) {
		number = num;
	}

	@Override
	public synchronized void execute(Bank bank, ObjectOutputStream dataOut)
			throws IOException {
		String owner = bank.getAccount(number).getOwner();
		try {
			dataOut.writeObject(owner);
		} catch (Exception e) {
			dataOut.writeObject(e);
		}
	}
}
