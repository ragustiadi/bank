package bank.rmi;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bank.BankDriver2.UpdateHandler;

public class RmiUpdateHandlerImpl extends UnicastRemoteObject implements RmiUpdateHandler {

	private UpdateHandler handler = null;

	public RmiUpdateHandlerImpl(UpdateHandler h) throws RemoteException {
		super();
		handler = h;
	}

	@Override
	public void accountChanged(String id) throws IOException {
		handler.accountChanged(id);
	}

}
