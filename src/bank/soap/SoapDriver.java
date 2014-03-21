package bank.soap;

import java.io.IOException;

import javax.jws.WebService;

import bank.Bank;
import bank.BankDriver;

@WebService
public class SoapDriver implements BankDriver {

	Bank bank = new BankServiceImpl();

	@Override
	public void connect(String[] args) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnect() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Bank getBank() {
		// TODO Auto-generated method stub
		return bank;
	}

}
