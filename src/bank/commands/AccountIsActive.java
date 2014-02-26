package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bank.Bank;

public class AccountIsActive implements Command, Serializable {

	private static final long serialVersionUID = -2791749733067769285L;
	private String number;

	public AccountIsActive(String num) {
		number = num;
	}

	@Override
	public synchronized void execute(Bank bank, ObjectOutputStream dataOut)
			throws IOException {
		boolean active = bank.getAccount(number).isActive();
		try {
			dataOut.writeObject(active);
		} catch (Exception e) {
			dataOut.writeObject(e);
		}
	}
}
