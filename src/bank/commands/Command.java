package bank.commands;

import java.io.IOException;

/*
 * Interface zum Erstellen eines Befehls, der vom Client an den Server geschickt
 * wird.
 */
public interface Command {
	public Object execute(bank.Bank bank) throws IOException;
}
