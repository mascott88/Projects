package com.app.bank.service;

import java.util.List;

import com.app.bank.exception.BusinessException;
import com.app.bank.model.Account;
import com.app.bank.model.Person;
import com.app.bank.model.Transactions;

public interface BankCRUDService {
	
	public int createPerson(Person person) throws BusinessException;
	public int deletePersonbyEmail(String email) throws BusinessException;
	public List<Account> CustomerAccessAccountApplication(Account registerForAccount, String email) throws BusinessException;
	public Transactions CustomerAccessDeposit(Transactions deposit, String email) throws BusinessException;
	public Transactions CustomerAccessWithdrawal(Transactions withdrawal, String email) throws BusinessException;	
	public List<Account> EmployeeAccessAccountApplication(Account account) throws BusinessException;
	public Transactions EmployeeAccessDeposit(Transactions depositResult) throws BusinessException;
	public Transactions EmployeeAccessWithdrawal(Transactions withdrawal) throws BusinessException;
	public int approveAccountByAccountNumber(int accountnumber) throws BusinessException;
	public int denyAccountByAccountNumber(int accountnumber) throws BusinessException;
	public Transactions postTransfer(String email, Transactions transaction) throws BusinessException;
	public Transactions acceptTransfer(String email, Transactions transaction) throws BusinessException;
	
}
