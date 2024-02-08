package com.app.bank.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.app.bank.exception.BusinessException;
import com.app.bank.model.Account;
import com.app.bank.model.Person;
import com.app.bank.model.Transactions;
import com.app.bank.service.AccountSearchService;

class AccountSearchServiceImplTest {

	private static AccountSearchService service;
	Account account = new Account();
	Person person = new Person();
	List<Account> accountList = new ArrayList<>();
	List<Transactions> transactionsList = new ArrayList<>();

	@BeforeAll
	public static void setUpService() {
		service = new AccountSearchServiceImpl();
	}
	
	@Test
	void testGetAllAccounts() throws BusinessException {
		assertEquals(accountList, service.getAllAccounts());
	}

	@Test
	void testGetCredentials() throws BusinessException {
		assertEquals(1, service.verifyCredentials(person.getEmail(), person.getPsswd()));
	}

	@Test
	void testGetAccountDetails() throws BusinessException {
		assertEquals(account, service.EmployeeAccessAccountDetails(1234567898));
	}

	@Test
	void testGetAccountDetailsCustomer() throws BusinessException {
		assertEquals(account, service.CustomerAccessAccountDetails(1234567898, "test@test.com"));
	}

	@Test
	void testGetAllPendingAccounts() throws BusinessException {
		assertEquals(accountList, service.getAllPendingAccounts());
	}

	@Test
	void testGetAllTransactions() throws BusinessException {
		assertEquals(transactionsList, service.getAllTransactions());
	}

	@Test
	void testGetTransfersByAccount() throws BusinessException {
		assertEquals(1, service.getTransfersByAccountNumber(1234567898));
	}
}
