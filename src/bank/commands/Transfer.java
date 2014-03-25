package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bank.Bank;

public class Transfer implements Command, Serializable {

	private static final long serialVersionUID = 3099315085690469923L;
	private String fromNumber;
	private String toNumber;
	private double amount;

	public Transfer(String from, String to, double a) {
		fromNumber = from;
		toNumber = to;
		amount = a;
	}

	@Override
	public void execute(Bank bank, ObjectOutputStream dataOut)
			throws IOException {
		try {
			bank.transfer(bank.getAccount(fromNumber),
					bank.getAccount(toNumber), amount);
			dataOut.writeObject(null);
		} catch (Exception e) {
			dataOut.writeObject(e);
		}
	}
}
