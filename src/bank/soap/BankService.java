/*
 * Copyright (c) 2000-2014 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved.
 */

package bank.soap;

import java.io.IOException;

import javax.jws.WebParam;
import javax.jws.WebService;

import bank.InactiveException;
import bank.OverdrawException;
import bank.soap.BankServiceImpl.CustomException;

/*
 * Interface für die Implementierung der Bankbefehle
 */
@WebService
public interface BankService {

	// ----- Bank methods
	String createAccount(@WebParam(name = "owner") String owner)
			throws IOException;

	boolean closeAccount(@WebParam(name = "number") String number)
			throws IOException;

	Object[] getAccountNumbers() throws IOException;

	String getAccount(@WebParam(name = "number") String number)
			throws IOException;

	void transfer(@WebParam(name = "a") String a,
			@WebParam(name = "b") String b,
			@WebParam(name = "amount") double amount) throws IOException,
			IllegalArgumentException, OverdrawException, InactiveException,
			CustomException;

	// ----- Account methods
//	String getNumber() throws IOException;	// redundant method

	String getOwner(@WebParam(name = "number") String number)
			throws IOException;

	boolean isActive(@WebParam(name = "number") String number)
			throws IOException;

	void deposit(@WebParam(name = "number") String number,
			@WebParam(name = "amount") double amount) throws IOException,
			IllegalArgumentException, InactiveException, CustomException;

	void withdraw(@WebParam(name = "number") String number,
			@WebParam(name = "amount") double amount) throws IOException,
			IllegalArgumentException, OverdrawException, InactiveException,
			CustomException;

	double getBalance(@WebParam(name = "number") String number)
			throws IOException;
}
