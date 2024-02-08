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
import com.app.bank.service.BankCRUDService;

class BankCRUDServiceImplTest {

	private static BankCRUDService service;
	Person person = new Person();
	List<Account> accountList = new ArrayList<>();
	Account account = new Account();
	Transactions deposit = new Transactions();
	Transactions withdrawal = new Transactions();

	@BeforeAll
	public static void setUpService() {
		service = new BankCRUDServiceImpl();
	}

	@Test
	void testCreatePerson() throws BusinessException {
		person.setPersonName("test");
		person.setEmail("test@test.edu");
		person.setPsswd("test");
		assertEquals(1, service.createPerson(person));
	}	

	@Test
	void testMakeDeposit() throws BusinessException {
		deposit.setAccountnumber(1234567898);
		deposit.setAmount(150.00);
		deposit.setTransactionstatus("approved");
		deposit.setTransactiontype("deposit");
		assertEquals(deposit, service.CustomerAccessDeposit(deposit, "test@test.com"));
	}

	@Test
	void testMakeWithdrawal() throws BusinessException {
		withdrawal.setAccountnumber(1234567898);
		withdrawal.setAmount(150.00);
		withdrawal.setTransactionstatus("approved");
		withdrawal.setTransactiontype("withdrawal");
		assertEquals(withdrawal, service.CustomerAccessWithdrawal(withdrawal, "test@test.com"));
	}

	@Test
	void testAccountApplication() throws BusinessException {
		account.setBalance(150);
		account.setDate("2021-03-24 21:54:57");
		account.setAccounttype("checking");
		account.setAccountStatus("pending");
		assertEquals(accountList, service.CustomerAccessAccountApplication(account, "test@test.edu"));
	}

	@Test
	void testApproveAccountByID() throws BusinessException {
		assertEquals(1, service.approveAccountByAccountNumber(1234567898));
	}

	@Test
	void testDenyAccountByID() throws BusinessException {
		assertEquals(1, service.denyAccountByAccountNumber(1234567899));
	}

	@Test
	void testPostTransfer() {
		fail("Not yet implemented");
	}

	@Test
	void testAcceptTransfer() {
		fail("Not yet implemented");
	}

}
