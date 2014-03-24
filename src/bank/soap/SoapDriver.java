package bank.soap;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.jws.WebService;

import bank.Account;
import bank.Bank;
import bank.BankDriver;
import bank.InactiveException;
import bank.OverdrawException;
import bank.soap.client.BankServiceImplService;
import bank.soap.client.IOException_Exception;
import bank.soap.client.IllegalArgumentException_Exception;
import bank.soap.client.InactiveException_Exception;
import bank.soap.client.OverdrawException_Exception;

@WebService
public class SoapDriver implements BankDriver {

	private bank.Bank bank = null;
	private bank.soap.client.BankServiceImpl port = null;

	@Override
	public void connect(String[] args) throws IOException {
		BankServiceImplService s = new BankServiceImplService();
		port = s.getBankServiceImplPort();
		bank = new LocalSoapBank(port);
		System.out.println("Client connected");
	}

	@Override
	public void disconnect() throws IOException {
		System.out.println("Client disconnected");
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	class LocalSoapBank implements bank.Bank {

		bank.soap.client.BankServiceImpl service = null;

		public LocalSoapBank(bank.soap.client.BankServiceImpl s) {
			service = s;
		}

		@Override
		public String createAccount(String owner) throws IOException {
			System.out.println("Driver: createAccount");
			String response = null;
			try {
				response = service.createAccount(owner);
			} catch (IOException_Exception e) {
//				e.printStackTrace();
				throw new IOException(e.getMessage());
			}
			return response;
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			System.out.println("Driver: closeAccount");
			boolean response = false;
			try {
				response = service.closeAccount(number);
			} catch (IOException_Exception e) {
				//	e.printStackTrace();
				throw new IOException(e.getMessage());
			}
			return response;
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			System.out.println("Driver: getAccountNumbers");
			Set<String> response = new HashSet<String>();
			try {
				for (Object acc : service.getAccountNumbers()) {
					response.add(acc.toString());
				}
			} catch (IOException_Exception e) {
				//	e.printStackTrace();
				throw new IOException(e.getMessage());
			}
			return response;
		}

		@Override
		public Account getAccount(String number) throws IOException {
			System.out.println("Driver: getAccount");
			Account response = null;
			try {
				String temp = service.getAccount(number);
				if (!temp.equals(""))
					response = new LocalSoapAccount(temp);
			} catch (IOException_Exception e) {
				//	e.printStackTrace();
				throw new IOException(e.getMessage());
			}
			return response;
		}

		@Override
		public void transfer(Account a, Account b, double amount)
				throws IOException, IllegalArgumentException,
				OverdrawException, InactiveException {
			System.out.println("Driver: transfer");
			try {
				service.transfer(a.getNumber(), b.getNumber(), amount);
			} catch (IOException_Exception e) {
				//	e.printStackTrace();
				throw new IOException(e.getMessage());
			} catch (InactiveException_Exception e) {
				//	e.printStackTrace();
				throw new InactiveException(e.getMessage());
			} catch (OverdrawException_Exception e) {
				//	e.printStackTrace();
				throw new OverdrawException(e.getMessage());
			} catch (IllegalArgumentException_Exception e) {
				//	e.printStackTrace();
				throw new IllegalArgumentException(e.getMessage());
			}
		}

		class LocalSoapAccount implements Account {

			String number = "default number";

			public LocalSoapAccount(String accountNumber) {
				number = accountNumber;
			}

			@Override
			public String getNumber() throws IOException {
				return number;
			}

			@Override
			public String getOwner() throws IOException {
				String response = null;
				try {
					response = service.getOwner(number);
				} catch (IOException_Exception e) {
					//	e.printStackTrace();
					throw new IOException(e.getMessage());
				}
				return response;
			}

			@Override
			public boolean isActive() throws IOException {
				boolean response = false;
				try {
					response = service.isActive(number);
				} catch (IOException_Exception e) {
					//	e.printStackTrace();
					throw new IOException(e.getMessage());
				}
				return response;
			}

			@Override
			public void deposit(double amount) throws IOException,
					IllegalArgumentException, InactiveException {
				try {
					service.deposit(number, amount);
				} catch (IOException_Exception e) {
					//	e.printStackTrace();
					throw new IOException(e.getMessage());
				} catch (InactiveException_Exception e) {
					//	e.printStackTrace();
					throw new InactiveException(e.getMessage());
				} catch (IllegalArgumentException_Exception e) {
					//	e.printStackTrace();
					throw new IllegalArgumentException(e.getMessage());
				}

			}

			@Override
			public void withdraw(double amount) throws IOException,
					IllegalArgumentException, OverdrawException,
					InactiveException {
				try {
					service.withdraw(number, amount);
				} catch (IOException_Exception e) {
					//	e.printStackTrace();
					throw new IOException(e.getMessage());
				} catch (OverdrawException_Exception e) {
					//	e.printStackTrace();
					throw new OverdrawException(e.getMessage());
				} catch (InactiveException_Exception e) {
					//	e.printStackTrace();
					throw new InactiveException(e.getMessage());
				} catch (IllegalArgumentException_Exception e) {
					//	e.printStackTrace();
					throw new IllegalArgumentException(e.getMessage());
				}

			}

			@Override
			public double getBalance() throws IOException {
				double response = 0.0;
				try {
					response = service.getBalance(number);
				} catch (IOException_Exception e) {
					//	e.printStackTrace();
					throw new IOException(e.getMessage());
				}
				return response;
			}

		}
	}

}
