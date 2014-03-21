package bank.HTML;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import bank.Account;
import bank.InactiveException;
import bank.OverdrawException;
import bank.HTML.HTMLServer.Type;

/*
 * Schnittstelle den SocketDriver um mit dem Server zu kommunizieren. Die
 * SocketBank speichert selber keine Daten, sondern sendet Anfragen (Commands)
 * an den Server.
 */
public class HTMLBank implements bank.Bank {

	private HTMLDriver driver = null;

	public HTMLBank(HTMLDriver d) {
		driver = d;
	}

	@Override
	public String createAccount(String owner) throws IOException {
		driver.get(Type.CREATE_ACCOUNT, owner);
		String response = driver.br.readLine();
		if (response.contains("x-bankException/IOException"))
			throw new IOException(driver.br.readLine());
		return response;
	}

	@Override
	public boolean closeAccount(String number) throws IOException {
		System.out.println("close");
		driver.get(Type.CLOSE_ACCOUNT, number);
		return Boolean.getBoolean(driver.br.readLine());
	}

	@Override
	public Set getAccountNumbers() throws IOException {
		driver.get(Type.GET_ACCOUNT_NUMBERS);
		Set<String> accounts = new HashSet<String>();
		String acc = driver.br.readLine();
		while (!acc.equals("*")) {
			System.out.println("number" + acc);
			accounts.add(acc);
			acc = driver.br.readLine();
		}
		return accounts;
	}

	@Override
	public Account getAccount(String number) throws IOException {
		System.out.println(number);
		driver.get(Type.GET_ACCOUNT, number);
		String asd = driver.br.readLine();
		System.out.println(asd);
		return new LocalAccount(asd);
	}

	@Override
	public void transfer(bank.Account a, bank.Account b, double amount)
			throws IOException, IllegalArgumentException, OverdrawException,
			InactiveException {
		System.out.println("transfer");
		driver.get(Type.TRANSFER, a.getNumber(), b.getNumber(),
				Double.toString(amount));

		if (driver.br.ready()) {
			String s = driver.br.readLine();
			while (!s.contains("x-bankException")) {
				s = driver.br.readLine();
			}
			if (s.contains("IllegalArgumentException"))
				throw new IllegalArgumentException(driver.br.readLine());
			else if (s.contains("OverdrawException"))
				throw new OverdrawException(driver.br.readLine());
			else if (s.contains("InactiveException"))
				throw new InactiveException(driver.br.readLine());
			else if (s.contains("IOException"))
				throw new IOException(driver.br.readLine());
			else
				System.out.println("default exception");
		}

	}

	/*
	 * LocalAccount ist ein DummyAccount, der nur die Nummer kennt. Die
	 * restlichen Daten werden vom Server angefragt. Die Nummer dient nur der
	 * eindeutigen Indentifikation des Accounts auf dem Server.
	 */
	class LocalAccount implements bank.Account {

		private String number;

		public LocalAccount(String num) {
			System.out.println("local " + num);
			number = num;
		}

		@Override
		public String getNumber() throws IOException {
			return number;
		}

		@Override
		public String getOwner() throws IOException {
			System.out.println("get owner");
			driver.get(Type.ACCOUNT_GET_OWNER, number);
//			driver.dataOut.writeObject(new AccountGetOwner(number));
			String owner = null;
//			try {
//				owner = (String) driver.dataIn.readObject();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
			return driver.br.readLine();
		}

		@Override
		public boolean isActive() throws IOException {
			System.out.println("is active");
			driver.get(Type.ACCOUNT_IS_ACTIVE, number);
			return Boolean.parseBoolean(driver.br.readLine());
		}

		@Override
		public void deposit(double amount) throws IOException,
				IllegalArgumentException, InactiveException {
			driver.get(Type.ACCOUNT_DEPOSIT, number, Double.toString(amount));
			String response = driver.br.readLine();
			if (response.contains("IllegalArgumentException"))
				throw new IllegalArgumentException(driver.br.readLine());
			else if (response.contains("InactiveException"))
				throw new InactiveException(driver.br.readLine());
			else if (response.contains("IOException"))
				throw new IOException(driver.br.readLine());
		}

		@Override
		public void withdraw(double amount) throws IOException,
				IllegalArgumentException, OverdrawException, InactiveException {
			driver.get(Type.ACCOUNT_WITHDRAW, number, Double.toString(amount));
			if (driver.br.ready()) {
				String s = driver.br.readLine();
				while (!s.contains("x-bankException")) {
					s = driver.br.readLine();
				}
				if (s.contains("IllegalArgumentException"))
					throw new IllegalArgumentException(driver.br.readLine());
				else if (s.contains("OverdrawException"))
					throw new OverdrawException(driver.br.readLine());
				else if (s.contains("InactiveException"))
					throw new InactiveException(driver.br.readLine());
				else if (s.contains("IOException"))
					throw new IOException(driver.br.readLine());
				else
					System.out.println("default exception");
			}
//			driver.dataOut.writeObject(new AccountWithdraw(number, amount));
//			try {
//				driver.dataIn.readObject();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
		}

		@Override
		public double getBalance() throws IOException {
			System.out.println(number);
			driver.get(Type.ACCOUNT_GET_BALLANCE, number);
			return Double.parseDouble(driver.br.readLine());
		}
	}

}
