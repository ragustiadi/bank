package bank.rmi;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.EventListener;

import bank.Bank;
import bank.BankDriver2;

public class RmiDriver implements BankDriver2, EventListener {

	RemoteBank bank = null;

	RmiUpdateHandler handler = null;

	@Override
	public void connect(String[] args) throws IOException {
		try {
			bank = (RemoteBank) Naming.lookup("rmi://localhost/BankService");
		} catch (NotBoundException e) {
			System.err.println("Connection not found.");
		}
	}

	@Override
	public void disconnect() throws IOException {
		bank.removeUpdateHandler(handler);
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	@Override
	public void registerUpdateHandler(UpdateHandler handler) throws IOException {
		this.handler = new RmiUpdateHandlerImpl(handler);
		bank.addUpdateHandler(this.handler);
	}

}
