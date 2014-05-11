package bank.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteBank extends bank.Bank, Remote {
	void addUpdateHandler(RmiUpdateHandler client) throws RemoteException;

	void removeUpdateHandler(RmiUpdateHandler client) throws RemoteException;
}
