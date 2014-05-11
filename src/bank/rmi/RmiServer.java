package bank.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RmiServer implements Remote {

	public static RemoteBankImpl bank = null;
	private static String url = "rmi://localhost:1099/BankService";

	public static void main(String[] args) throws RemoteException {
		try {
			LocateRegistry.createRegistry(1099);
			System.out.println("Registry created");

			bank = new RemoteBankImpl(new bank.common.ServerBank());
			System.out.println("Bank created");

			Naming.rebind(url, bank);
			System.out.println("Bank bound to URL " + url);

			System.out.println("Server ready");
		} catch (RemoteException e) {
			System.err.println("Registry already exists");
		} catch (MalformedURLException e) {
			System.err.println("Invalid URL");
		}
	}

}
