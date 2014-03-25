package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import bank.Bank;

public class AccountWithdraw implements Command, Serializable {

	private static final long serialVersionUID = -7881429959569323351L;
	private String number;
	private double amount;

	public AccountWithdraw(String num, double am) {
		number = num;
		amount = am;
	}

	@Override
	public void execute(Bank bank, ObjectOutputStream dataOut)
			throws IOException {
		try {
			bank.getAccount(number).withdraw(amount);
			dataOut.writeObject(null);
		} catch (Exception e) {
			dataOut.writeObject(e);
		}
	}
}
