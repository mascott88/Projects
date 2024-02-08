package com.app.bank.service;

import java.util.List;

import com.app.bank.exception.BusinessException;
import com.app.bank.model.Account;
import com.app.bank.model.Transactions;

public interface EmployeeAccountService {
	
	public Transactions EmmployeeAccessDeposit();
	public Transactions EmployeeAccessWithdrawal();
	public Account EmployeeAccessSearch();
	public List<Account> EmployeeAccessAccountList();
	public List<Account> EmployeeAccessApproveAccounts();
	public List<Transactions> EmployeeAccessTransactionsList();
	public List<Account> EmployeeAccessAccountApplication() throws BusinessException;	
	
}
