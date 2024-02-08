package com.app.bank.service.impl;


import java.util.List;

import com.app.bank.dao.BankCRUDDAO;
import com.app.bank.dao.impl.BankCRUDDAOImpl;
import com.app.bank.exception.BusinessException;
import com.app.bank.model.Account;
import com.app.bank.model.Person;
import com.app.bank.model.Transactions;
import com.app.bank.service.BankCRUDService;
import com.app.bank.service.LogService;

public class BankCRUDServiceImpl implements BankCRUDService {

	private BankCRUDDAO bankcruddao = new BankCRUDDAOImpl();
	LogService logservice = new LogService();

	public int createPerson(Person person) throws BusinessException {
		if (!PersonValidations.isValidPersonName(person.getPersonName())) {
			logservice.InfoLogService("Entered name " + person.getPersonName() + " is Invalid");
			throw new BusinessException();
		}
		if (!PersonValidations.isValidPersonEmail(person.getEmail())) {
			logservice.InfoLogService("Entered email " + person.getEmail() + " is Invalid");
			throw new BusinessException();
		}
		if (!PersonValidations.isValidPersonPassword(person.getPsswd())) {
			logservice.InfoLogService("Entered password " + person.getPsswd() + " is Invalid");
			throw new BusinessException();
		}

		return (bankcruddao.createPerson(person));
	}	

	@Override
	public Transactions CustomerAccessDeposit(Transactions deposit, String email) throws BusinessException {
		if (!PersonValidations.isValidTransactionAmount(deposit.getAmount())) {
			logservice.InfoLogService("Entered amount " + deposit.getAmount() + " is Invalid");
			throw new BusinessException();
		}
		if (!PersonValidations.isValidAccountNumber(deposit.getAccountnumber())) {
			logservice.InfoLogService("Entered account number " + deposit.getAccountnumber() + " is Invalid");
			throw new BusinessException();
		}

		return (bankcruddao.CustomerAccessDeposit(deposit, email));
	}

	@Override
	public Transactions CustomerAccessWithdrawal(Transactions withdrawal, String email) throws BusinessException {
		if (!PersonValidations.isValidTransactionAmount(withdrawal.getAmount())) {
			logservice.InfoLogService("Entered amount " + withdrawal.getAmount() + " is Invalid");
			throw new BusinessException();
		}
		if (!PersonValidations.isValidAccountNumber(withdrawal.getAccountnumber())) {
			logservice.InfoLogService("Entered account number " + withdrawal.getAccountnumber() + " is Invalid");
			throw new BusinessException();
		}

		return (bankcruddao.CustomerAccessWithdrawal(withdrawal, email));
	}

	@Override
	public List<Account> CustomerAccessAccountApplication(Account account, String email) throws BusinessException {
		if (!PersonValidations.isValidAccountType(account.getAccounttype())) {
			logservice.InfoLogService("Entered Account Type " + account.getAccounttype() + " is Invalid");
			throw new BusinessException();
		}
		if (!PersonValidations.isValidTransactionAmount(account.getBalance())) {
			logservice.InfoLogService("Entered Amount " + account.getBalance() + " is Invalid");
			throw new BusinessException();
		}
			
			return (bankcruddao.CustomerAccessAccountApplication(account, email));
	}

	@Override
	public int approveAccountByAccountNumber(int accountnumber) throws BusinessException {
		if (!PersonValidations.isValidAccountNumber(accountnumber)) {
			logservice.InfoLogService("Entered account number " + accountnumber + " is Invalid");
			throw new BusinessException();
		}

		return (bankcruddao.approveAccountByAccountNumber(accountnumber));
	}

	@Override
	public int denyAccountByAccountNumber(int accountnumber) throws BusinessException {
		if (!PersonValidations.isValidAccountNumber(accountnumber)) {
			logservice.InfoLogService("Entered account number " + accountnumber + " is Invalid");
			throw new BusinessException();
		}

		return (bankcruddao.denyAccountByAccountNumber(accountnumber));
	}

	@Override
	public Transactions postTransfer(String email, Transactions post) throws BusinessException {
		if (!PersonValidations.isValidAccountNumber(post.getAccountnumber())) {
			logservice.InfoLogService("Entered account number " + post.getAccountnumber() + " is Invalid");
			throw new BusinessException();
		}
		if (!PersonValidations.isValidTransactionAmount(post.getAmount())) {
			logservice.InfoLogService("Entered amount " + post.getAmount() + " is Invalid");
			throw new BusinessException();
		}

		return (bankcruddao.postTransfer(email, post));
	}

	@Override
	public Transactions acceptTransfer(String email, Transactions accept) throws BusinessException {

		return (bankcruddao.acceptTransfer(email, accept));
	}

	@Override
	public int deletePersonbyEmail(String loginEmail) throws BusinessException {
		
		return (bankcruddao.deletePersonbyEmail(loginEmail));
		
	}

	@Override
	public Transactions EmployeeAccessDeposit(Transactions depositResult) throws BusinessException {
		if (!PersonValidations.isValidTransactionAmount(depositResult.getAmount())) {
			logservice.InfoLogService("Entered amount " + depositResult.getAmount() + " is Invalid");
			throw new BusinessException();
		}
		if (!PersonValidations.isValidAccountNumber(depositResult.getAccountnumber())) {
			logservice.InfoLogService("Entered account number " + depositResult.getAccountnumber() + " is Invalid");
			throw new BusinessException();
		}

		return (bankcruddao.EmployeeAccessDeposit(depositResult));
	}

	@Override
	public Transactions EmployeeAccessWithdrawal(Transactions withdrawal) throws BusinessException {
		if (!PersonValidations.isValidTransactionAmount(withdrawal.getAmount())) {
			logservice.InfoLogService("Entered amount " + withdrawal.getAmount() + " is Invalid");
			throw new BusinessException();
		}
		if (!PersonValidations.isValidAccountNumber(withdrawal.getAccountnumber())) {
			logservice.InfoLogService("Entered account number " + withdrawal.getAccountnumber() + " is Invalid");
			throw new BusinessException();
		}

		return (bankcruddao.EmployeeAccessWithdrawal(withdrawal));
	}

	@Override
	public List<Account> EmployeeAccessAccountApplication(Account account) throws BusinessException {
		if (!PersonValidations.isValidAccountType(account.getAccounttype())) {
			logservice.InfoLogService("Entered Account Type " + account.getAccounttype() + " is Invalid");
			throw new BusinessException();
		}
		if (!PersonValidations.isValidTransactionAmount(account.getBalance())) {
			logservice.InfoLogService("Entered Amount " + account.getBalance() + " is Invalid");
			throw new BusinessException();
		}
			
			return (bankcruddao.EmployeeAccessAccountApplication(account));
	}
}