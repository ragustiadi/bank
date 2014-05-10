package bank.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RmiServer implements Remote {

	public static RemoteBankImpl bank = null;

	public static void main(String[] args) throws RemoteException {
		try {
			LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			System.out.println("Registry already exists.");
		}

		try {
			bank = new RemoteBankImpl(new bank.common.ServerBank());
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		try {
			Naming.rebind("rmi://localhost:1099/BankService", bank);
		} catch (MalformedURLException e) {
			System.out.println("Invalid URL");
		}

	}

}
