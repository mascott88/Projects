package com.app.bank.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.app.bank.exception.BusinessException;
import com.app.bank.model.Account;
import com.app.bank.model.Transactions;
import com.app.bank.service.EmployeeAccountService;
import com.app.bank.service.AccountSearchService;
import com.app.bank.service.BankCRUDService;
import com.app.bank.service.CustomerAccountService;
import com.app.bank.service.LogService;

public class EmployeeAccessServiceImpl implements EmployeeAccountService {

	BankCRUDService bankcrudservice = new BankCRUDServiceImpl();
	AccountSearchService searchservice = new AccountSearchServiceImpl();
	LogService logservice = new LogService();
	Scanner scanner = new Scanner(System.in);
	DecimalFormat d = new DecimalFormat("'$'#.00");

	@Override
	public Transactions EmmployeeAccessDeposit() {

		Transactions depositResult = new Transactions();
		double depositAmount = 0;

		try {
			logservice.InfoLogService("Enter Account number");
			depositResult.setAccountnumber(Integer.parseInt(scanner.nextLine()));
		} catch (NumberFormatException e) {
			logservice.InfoLogService("Entered account number is Invalid");
			return null;
		}
		try {
			logservice.InfoLogService("Enter amount");
			depositResult.setAmount(Double.parseDouble(scanner.nextLine()));
		} catch (NumberFormatException e) {
			logservice.InfoLogService("Entered amount " + String.valueOf(depositAmount) + " is Invalid");
			return null;
		}
		try {
			depositResult.setTransactiontype("deposit");
			depositResult.setTransactionstatus("approved");
			depositResult = bankcrudservice.EmployeeAccessDeposit(depositResult);
			if (depositResult != null) {
				logservice.InfoLogService("The amount of " + d.format(depositResult.getAmount())
						+ " was deposited to account number " + depositResult.getAccountnumber());
				return depositResult;
			}
		} catch (BusinessException e) {
			logservice.WarnLogService(e.getMessage());
			return null;
		} catch (NumberFormatException e) {
			logservice.InfoLogService("Entered amount " + String.valueOf(depositAmount) + " is Invalid");
			return null;
		}

		return null;
	}

	@Override
	public Transactions EmployeeAccessWithdrawal() {

		Transactions withdrawalResult = new Transactions();

		try {
			logservice.InfoLogService("Enter Account number");
			withdrawalResult.setAccountnumber(Integer.parseInt(scanner.nextLine()));
		} catch (NumberFormatException e) {
			logservice.InfoLogService("Entered account number is Invalid");
			return null;
		}
		try {
			logservice.InfoLogService("Enter amount");
			withdrawalResult.setAmount(Double.parseDouble(scanner.nextLine()));
		} catch (NumberFormatException e) {
			logservice
					.InfoLogService("Entered amount " + String.valueOf(withdrawalResult.getAmount()) + " is Invalid.");
			return null;
		}
		try {
			withdrawalResult.setTransactiontype("withdrawal");
			withdrawalResult.setTransactionstatus("approved");
			withdrawalResult = bankcrudservice.EmployeeAccessWithdrawal(withdrawalResult);
			if (withdrawalResult != null) {
				logservice.InfoLogService("The amount of " + d.format(withdrawalResult.getAmount())
						+ " was withdrawn from account number " + withdrawalResult.getAccountnumber());
				return withdrawalResult;
			}
		} catch (NumberFormatException e) {
			logservice.InfoLogService("Entered amount " + String.valueOf(withdrawalResult.getAmount()) + " is Invalid");
			return null;
		} catch (BusinessException e) {
			logservice.WarnLogService(e.getMessage());
			return null;
		}

		return null;
	}

	@Override
	public Account EmployeeAccessSearch() {

		int employeeAccountSearch = 0;
		Account employeeAccountDetails = null;

		try {
			logservice.InfoLogService("Enter Account number");
			employeeAccountSearch = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			logservice.InfoLogService("Entered account number is Invalid");
			return null;
		}
		try {
			employeeAccountDetails = searchservice.EmployeeAccessAccountDetails(employeeAccountSearch);
			if (employeeAccountDetails != null) {
				logservice.InfoLogService(employeeAccountDetails.toString());
				return employeeAccountDetails;
			}
		} catch (BusinessException e) {
			logservice.WarnLogService(e.getMessage());
			return null;
		}

		return null;
	}

	@Override
	public List<Account> EmployeeAccessAccountList() {

		List<Account> allAccountsStatus = new ArrayList<>();

		try {
			allAccountsStatus = searchservice.getAllAccounts();
			if (allAccountsStatus != null) {
				allAccountsStatus.stream().forEach(x -> logservice.InfoLogService(x));
				return allAccountsStatus;
			}
		} catch (BusinessException e) {
			logservice.WarnLogService(e.getMessage());
			return null;
		}

		return null;
	}

	@Override
	public List<Account> EmployeeAccessApproveAccounts() {

		int option = 0;

		do {			
			logservice.InfoLogService("=============================================================================================================================================================================");
			logservice.InfoLogService("Approve or Deny");
			logservice.InfoLogService("=============================================================================================================================================================================");
			logservice.InfoLogService("1. Approve account");
			logservice.InfoLogService("2. Deny account");
			logservice.InfoLogService("3. Go back");
			try {
				option = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
			}
			int pendingAccount = 0;
			switch (option) {
			case 1:
				try {
					logservice.InfoLogService("Enter Account number");
					pendingAccount = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					logservice.InfoLogService("Entered account number is Invalid");
					break;
				}
				try {
					bankcrudservice.approveAccountByAccountNumber(pendingAccount);
					logservice.InfoLogService("Account number " + pendingAccount + " has been approved");
				} catch (BusinessException e) {
					logservice.WarnLogService(e.getMessage());
					break;
				}
				break;
			case 2:
				logservice.InfoLogService("Enter Account number");
				try {
					pendingAccount = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					logservice.InfoLogService("Entered account number is Invalid");
					break;
				}
				try {
					int denyAccount = bankcrudservice.denyAccountByAccountNumber(pendingAccount);
					if (denyAccount != 0) {
						logservice.InfoLogService("Account number " + pendingAccount + " has been denied");
					}
				} catch (BusinessException e) {
					logservice.WarnLogService(e.getMessage());
					break;
				}
				break;
			case 3:
				break;
			default:
				logservice.InfoLogService("Invalid Choice...Please enter a choice between 1-3 only.");
				break;
			}
		} while (option != 3);

		return null;
	}

	@Override
	public List<Account> EmployeeAccessAccountApplication() throws BusinessException {

		Account account = new Account();
		int option = 0;
		int choice = 0;
		int result = 0;

		do {			
			logservice.InfoLogService("=============================================================================================================================================================================");
			logservice.InfoLogService("Enter choice for customer");
			logservice.InfoLogService("1. New customer");
			logservice.InfoLogService("2. Existing customer");
			logservice.InfoLogService("3. Back to main menu");
			choice = Integer.parseInt(scanner.nextLine());
			switch (choice) {
			case 1:
				CustomerAccountService customerservice = new CustomerAccessServiceImpl();
				return customerservice.CustomerAccessRegisterNewLogin();
			case 2:
				do {
					try {
						logservice.InfoLogService("Enter account holder's eMail address");
						account.setPersonemail(scanner.nextLine());
						result = searchservice.getAccountByEmail(account.getPersonemail());
					} catch (BusinessException e) {
						logservice.WarnLogService(e.getMessage());
						return null;
					}
					if (result != 0) {						
						logservice.InfoLogService("=============================================================================================================================================================================");
						logservice.InfoLogService("ROC Bank account registration");
						logservice.InfoLogService("=============================================================================================================================================================================");
						logservice.InfoLogService("Enter choice for account type");
						logservice.InfoLogService("1. Apply for checking account");
						logservice.InfoLogService("2. Apply for savings account");
						logservice.InfoLogService("3. Go back");
						try {
							option = Integer.parseInt(scanner.nextLine());
						} catch (NumberFormatException e) {
							option = 0;
						}
						switch (option) {
						case 1:
							account.setAccounttype("checking");
							return apply(account);
						case 2:
							account.setAccounttype("savings");
							return apply(account);
						case 3:
							return null;
						default:
							logservice.InfoLogService("Invalid Choice...Please enter a choice between 1-3 only.");
							option = 0;
							break;
						}
					}
				} while (option != 3);
				break;
			case 3:
				return null;
			default:
				logservice.InfoLogService("Invalid Choice...Please enter a choice between 1-3 only.");
				option = 0;
				break;
			}
		} while (option != 3);

		return null;
	}

	private List<Account> apply(Account account) throws BusinessException {

		double amount = 0.0;
		account.setAccountStatus("pending");

		try {
			logservice.InfoLogService("Enter starting balance");
			amount = Double.parseDouble(scanner.nextLine());
			account.setBalance(amount);
		} catch (NumberFormatException e) {
			logservice.InfoLogService("Entered amount " + String.valueOf(amount) + " is Invalid");
			return null;
		}
		try {
			List<Account> newAccount = bankcrudservice.EmployeeAccessAccountApplication(account);
			if (newAccount != null) {				
				logservice.InfoLogService("*****************************************************************************************************************************************************************************");
				logservice.InfoLogService("Account registration successful, Welcome to ROC Banking.");
				logservice.InfoLogService("*****************************************************************************************************************************************************************************");
				logservice.InfoLogService("New " + account.getAccounttype()
						+ " account application received. Listing pending accounts for " + account.getPersonemail()
						+ " below...");
				for (Account a : newAccount)
					logservice.InfoLogService(
							"account number: " + a.getAccountnumber() + ", opening date: " + a.getOpeningdate()
									+ ", type:" + a.getAccounttype() + ", balance: " + d.format(a.getBalance()) + ", status: " + a.getAccountStatus() + "\n");
				return newAccount;
			} else {
				logservice.InfoLogService("There are no pending accounts");
			}
		} catch (BusinessException e) {
			logservice.WarnLogService(e.getMessage());
			return null;
		}

		return null;
	}

	@Override
	public List<Transactions> EmployeeAccessTransactionsList() {

		List<Transactions> allTransactionsList = new ArrayList<>();

		try {
			allTransactionsList = searchservice.getAllTransactions();
			if (allTransactionsList.size() != 0) {
				allTransactionsList.stream().forEach(x -> logservice.InfoLogService(x));
				return allTransactionsList;
			}
		} catch (BusinessException e) {
			logservice.WarnLogService(e.getMessage());
			return null;
		}

		return null;
	}
}