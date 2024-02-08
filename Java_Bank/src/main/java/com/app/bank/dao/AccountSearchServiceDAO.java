	package com.app.bank.dao;

import java.util.List;

import com.app.bank.exception.BusinessException;
import com.app.bank.model.Account;
import com.app.bank.model.Transactions;

public interface AccountSearchServiceDAO {
	
	public Account EmployeeAccessAccountDetails(int account) throws BusinessException;
	public Account CustomerAccessAccountDetails(int accountnumber, String amount) throws BusinessException;		
	public List<Account> getAllAccounts() throws BusinessException;		
	public int getAccountByEmail(String email) throws BusinessException;
	public List<Account> getAllPendingAccounts() throws BusinessException;	
	public List<Transactions> getAllTransactions() throws BusinessException;
	public List<Transactions> getTransfersByAccountNumber(int accountnumber) throws BusinessException;	
	public int verifyCredentials(String email, String psswd) throws BusinessException;	
	
}
