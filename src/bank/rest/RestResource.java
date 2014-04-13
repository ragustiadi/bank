package bank.rest;

import java.io.IOException;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/bank")
public class RestResource {

	private Bank bank;

	public RestResource() {
		bank = new bank.common.ServerBank();
	}

	@POST
	@Path("/accounts")
	@Produces("text/plain")
	public String createAccount(@QueryParam("owner") String owner) throws IOException {
		System.out.println("Create account for " + owner);
		return bank.createAccount(owner);
	}

	@DELETE
	@Path("/accounts/{number}")
	@Produces("text/plain")
	public String closeAccount(@PathParam("number") String number) throws IOException {
		System.out.println("close account number " + number);
		return Boolean.toString(bank.closeAccount(number));
	}

	@GET
	@Path("/accounts")
	@Produces("text/plain")
	public String getAccountNumbers() throws IOException {
		System.out.println("get all accounts");
		Set<String> accounts = bank.getAccountNumbers();
		StringBuilder sb = new StringBuilder();
		if (accounts.size() != 0) {
			sb = new StringBuilder();
			for (String acc : accounts)
				sb.append(acc + "\n");
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	@POST
	@Path("/transfer")
	@Produces("text/plain")
	public Response transfer(@QueryParam("from") String from, @QueryParam("to") String to,
			@QueryParam("amount") double amount) throws IOException {
		System.out.println("transfer from " + from + " to " + to);
		try {
			bank.transfer(bank.getAccount(from), bank.getAccount(to), amount);
			return Response.ok().build();
		} catch (IllegalArgumentException e) {
			return Response.ok("IllegalArgument").build();
		} catch (OverdrawException e) {
			return Response.ok("Overdraw").build();
		} catch (InactiveException e) {
			return Response.ok("Inactive").build();
		}
	}

	@GET
	@Path("/accounts/{number}")
	@Produces("text/plain")
	public Response getOwner(@PathParam("number") String number) throws IOException {
		System.out.println("Get owner of " + number);
		bank.Account acc = bank.getAccount(number);
		if (acc != null) // wird gebraucht zum Überprüfen, ob die Accountnummer gültig ist
			return Response.ok(acc.getOwner()).build();
		else
			return Response.ok("null").build();
	}

	@HEAD
	@Path("/accounts/{number}")
	@Produces("text/plain")
	public Response isActive(@PathParam("number") String number) throws IOException {
		System.out.println("is " + number + " active");
		boolean temp = bank.getAccount(number).isActive();
		System.out.println(temp);
		if (temp)
			return Response.ok().build();
		else
			return Response.notModified().status(Status.GONE).build();
	}

	@PUT
	@Path("/accounts/{number}")
	@Produces("text/plain")
	public String accountOperation(@PathParam("number") String number,
			@QueryParam("operation") String operation, @QueryParam("amount") double amount)
			throws IOException {
		try {
			if (operation.equals("deposit")) {
				System.out.println("deposit to " + number);
				bank.getAccount(number).deposit(amount);
			} else if (operation.equals("withdraw")) {
				System.out.println("withdraw from " + number);
				bank.getAccount(number).withdraw(amount);
			}
		} catch (IllegalArgumentException e) {
			return "IllegalArgument";
		} catch (OverdrawException e) {
			return "Overdraw";
		} catch (InactiveException e) {
			return "Inactive";
		}
		return "operation completed";
	}

	@POST
	@Path("/accounts/{number}")
	@Produces("text/plain")
	public String getBalance(@PathParam("number") String number) throws IOException {
		System.out.println("get balance from " + number);
		return Double.toString(bank.getAccount(number).getBalance());
	}
}
