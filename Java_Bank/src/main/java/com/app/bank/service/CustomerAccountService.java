package com.app.bank.service;

import java.util.List;

import com.app.bank.exception.BusinessException;
import com.app.bank.model.Account;
import com.app.bank.model.Transactions;

public interface CustomerAccountService {
	
	public Transactions CustomerAccessDeposit(String loginEmail);
	public  Transactions CustomerAccessWithdrawal(String loginEmail);
	public Account CustomerAccessAccountDetails(String loginEmail);
	public Transactions CustomerAccessTransferMoney(String loginEmail);
	public List<Account> CustomerAccessNewAccountApplication(String loginEmail) throws BusinessException;
	public List<Account> CustomerAccessRegisterNewLogin();
	
}
