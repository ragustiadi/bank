package bank.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import bank.BankDriver2.UpdateHandler;

public interface RemoteBank extends bank.Bank, Remote {
	void addUpdateHandler(UpdateHandler client) throws RemoteException;

	void removeUpdateHandler(UpdateHandler client) throws RemoteException;
}
