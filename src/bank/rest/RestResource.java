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
import javax.ws.rs.core.Response;

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

	@PUT
	@Path("/accounts/{owner}")
	@Produces("text/plain")
	public Response createAccount(@PathParam("owner") String owner) {
		System.out.println("Create account for " + owner);
		try {
			return Response.ok(bank.createAccount(owner)).build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.notModified().build();
	}

	@DELETE
	@Path("/accounts/{number}")
	@Produces("text/plain")
	public Response closeAccount(@PathParam("number") String number) {
		System.out.println("close account number " + number);
		try {
			if (bank.closeAccount(number))
				return Response.ok("true").build();
			else
				return Response.notModified("false").build();
		} catch (IOException e) {
			return Response.notModified("false").build();
		}
	}

	@GET
	@Path("/accounts")
	@Produces("text/plain")
	public String getAccountNumbers() {
		System.out.println("get all accounts");
		Set<String> accounts;
		try {
			accounts = bank.getAccountNumbers();
			if (accounts.size() != 0) {
				StringBuilder sb = new StringBuilder();
				for (String acc : accounts)
					sb.append(acc + "\n");
				sb.deleteCharAt(sb.length() - 1);
				return sb.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	@POST
	@Path("/accounts/{from}&{to}&{amount}")
	@Produces("text/plain")
	public Response transfer(@PathParam("from") String from,
			@PathParam("to") String to, @PathParam("amount") double amount) {
		System.out.println("transfer from " + from + " to " + to);
		try {
			bank.transfer(bank.getAccount(from), bank.getAccount(to), amount);
			return Response.ok("true").build();
		} catch (IllegalArgumentException e) {
			return Response.notModified(e.getMessage()).build();
		} catch (IOException e) {
			return Response.notModified(e.getMessage()).build();
		} catch (OverdrawException e) {
			return Response.notModified(e.getMessage()).build();
		} catch (InactiveException e) {
			return Response.notModified(e.getMessage()).build();
		}
	}

	@GET
	@Path("/accounts/{number}")
	@Produces("text/plain")
	public Response getOwner(@PathParam("number") String number) {
		System.out.println("get owner for " + number);
		try {
			String res = bank.getAccount(number).getOwner();
			return Response.ok(res).build();
		} catch (IOException e) {
			return Response.notModified("IO").build();
		}
	}

	@HEAD
	@Path("/accounts/{number}")
	@Produces("text/plain")
	public Response isActive(@PathParam("number") String number) {
		System.out.println("is " + number + " active");
		try {
			return Response.ok(bank.getAccount(number).isActive()).build();
		} catch (IOException e) {
			return Response.notModified("IO").build();
		}
	}

	@PUT
	@Path("/accounts/{number}/deposit={amount}")
	@Produces("text/plain")
	public Response deposit(@PathParam("number") String number,
			@PathParam("amount") String amount) {
		System.out.println("deposit to " + number);
		try {
			bank.getAccount(number).deposit(Double.parseDouble(amount));
		} catch (IllegalArgumentException e) {
			return Response.notModified("IllegalArgument").build();
		} catch (IOException e) {
			return Response.notModified("IO").build();
		} catch (InactiveException e) {
			return Response.notModified("Inactive").build();
		}
		return Response.ok().build();
	}

	@PUT
	@Path("/accounts/{number}/withdraw={amount}")
	@Produces("text/plain")
	public Response withdraw(@PathParam("number") String number,
			@PathParam("amount") String amount) {
		System.out.println("withdraw from " + number);
		try {
			bank.getAccount(number).withdraw(Double.parseDouble(amount));
		} catch (IllegalArgumentException e) {
			return Response.notModified("IllegalArgument").build();
		} catch (IOException e) {
			return Response.notModified("IO").build();
		} catch (OverdrawException e) {
			return Response.notModified("Overdraw").build();
		} catch (InactiveException e) {
			return Response.notModified("Inactive").build();
		}
		return Response.ok().build();
	}

	@GET
	@Path("/accounts/{number}/balance")
	@Produces("text/plain")
	public Response getBalance(@PathParam("number") String number) {
		System.out.println("get balance from " + number);
		try {
			return Response.ok(
					String.valueOf(bank.getAccount(number).getBalance()))
					.build();
		} catch (IOException e) {
			return Response.notModified("IO").build();
		}
	}
}
