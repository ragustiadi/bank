package bank.rest;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import bank.Bank;
import bank.BankDriver;
import bank.InactiveException;
import bank.OverdrawException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class RestDriver implements BankDriver {

	bank.Bank bank;

	@Override
	public void connect(String[] args) throws IOException {
		Client c = Client.create();
		bank = new RestBank(c, "http://localhost:9998/bank");
	}

	@Override
	public void disconnect() throws IOException {
		System.out.println("Client disconnected.");
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	class RestBank implements bank.Bank {
		public Client client;
		public String serverUrl;

		public RestBank(Client c, String url) {
			client = c;
			serverUrl = url;
		}

		@Override
		public String createAccount(String owner) throws IOException {
			WebResource resource = client.resource(serverUrl + "/accounts").queryParam("owner",
					owner);
			return resource.post(String.class);
		}

		@Override
		public boolean closeAccount(String number) throws IOException {
			WebResource resource = client.resource(serverUrl + "/accounts/" + number);
			return Boolean.parseBoolean(resource.delete(String.class));
		}

		@Override
		public Set<String> getAccountNumbers() throws IOException {
			WebResource resource = client.resource(serverUrl + "/accounts");
			String response = resource.get(String.class);
			Set<String> set = new HashSet<String>();
			for (String s : response.split("\n")) {
				if (!s.equals(""))
					set.add(s);
			}
			return set;
		}

		@Override
		public Account getAccount(String number) throws IOException {
			if (number.equals("") || number.contains(" ")) // ungültige Werte für Kontonummern könnten
				number = "InvalidNumber"; // eigentlich hier abgefangen und nicht weitergeleitet werden
			Account acc = new Account(number);
			return acc.getOwner() == null ? null : acc;
		}

		@Override
		public void transfer(bank.Account a, bank.Account b, double amount) throws IOException,
				IllegalArgumentException, OverdrawException, InactiveException {
			WebResource resource = client.resource(serverUrl + "/transfer")
					.queryParam("from", a.getNumber()).queryParam("to", b.getNumber())
					.queryParam("amount", Double.toString(amount));
			String response = resource.post(String.class);
			switch (response) {
			case "IllegalArgument":
				throw new IllegalArgumentException();
			case "IO":
				throw new IOException();
			case "Overdraw":
				throw new OverdrawException();
			case "Inactive":
				throw new InactiveException();
			}
		}

		class Account implements bank.Account {

			private String number;

			public Account(String nr) {
				number = nr;
			}

			@Override
			public String getNumber() throws IOException {
				return number;
			}

			@Override
			public String getOwner() throws IOException {
				WebResource resource = client.resource(serverUrl + "/accounts/" + number);
				String response = resource.get(String.class);
				return response.equals("null") ? null : response;
			}

			@Override
			public boolean isActive() throws IOException {
				WebResource resource = client.resource(serverUrl + "/accounts/" + number);
				ClientResponse response = resource.head();
				System.out.println("status: " + response.getStatus());
				if (response.getStatus() == 200)
					return true;
				else
					return false;
			}

			@Override
			public void deposit(double amount) throws IOException, IllegalArgumentException,
					InactiveException {
				WebResource resource = client.resource(serverUrl + "/accounts/" + number)
						.queryParam("operation", "deposit")
						.queryParam("amount", Double.toString(amount));
				String response = resource.put(String.class);
				switch (response) {
				case "IllegalArgument":
					throw new IllegalArgumentException();
				case "Inactive":
					throw new InactiveException();
				}
			}

			@Override
			public void withdraw(double amount) throws IOException, IllegalArgumentException,
					OverdrawException, InactiveException {
				WebResource resource = client.resource(serverUrl + "/accounts/" + number)
						.queryParam("operation", "withdraw")
						.queryParam("amount", Double.toString(amount));
				String response = resource.put(String.class);
				switch (response) {
				case "IllegalArgument":
					throw new IllegalArgumentException();
				case "Overdraw":
					throw new OverdrawException();
				case "Inactive":
					throw new InactiveException();
				}
			}

			@Override
			public double getBalance() throws IOException {
				WebResource resource = client.resource(serverUrl + "/accounts/" + number);
				String response = resource.post(String.class);
				return Double.parseDouble(response);
			}
		}
	}
}
