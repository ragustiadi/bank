package bank.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;

public interface Command {
	public void execute(bank.Bank bank, ObjectOutputStream dataOut)
			throws IOException;
}
