package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bank.Bank;

public class CreateAccount implements Command, Serializable {

	private static final long serialVersionUID = 7654565592734721896L;
	private String owner = null;

	public CreateAccount(String o) {
		owner = o;
	}

	public synchronized void execute(Bank bank, ObjectOutputStream dataOut) {
		try {
			String number = bank.createAccount(owner);
			dataOut.writeObject(number);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
