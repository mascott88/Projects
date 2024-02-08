package com.app.bank.service.impl;

import java.util.NoSuchElementException;
import java.util.Scanner;

import com.app.bank.exception.BusinessException;
import com.app.bank.model.Person;
import com.app.bank.service.EmployeeAccountService;
import com.app.bank.service.AccountSearchService;
import com.app.bank.service.BankCRUDService;
import com.app.bank.service.CustomerAccountService;
import com.app.bank.service.LogService;
import com.app.bank.service.SystemAccess;

public class SystemAccessImpl implements SystemAccess {

	CustomerAccountService customerservice = new CustomerAccessServiceImpl();
	EmployeeAccountService employeeservice = new EmployeeAccessServiceImpl();
	AccountSearchService searchservice = new AccountSearchServiceImpl();
	BankCRUDService bankcrudservice = new BankCRUDServiceImpl();
	LogService logservice = new LogService();
	Scanner scanner = new Scanner(System.in);
	Person person = new Person();

	public void ROCBANKMAIN() throws BusinessException {

		logservice.InfoLogService("*************************");
		logservice.InfoLogService("*****R.O.C. Banking******");
		int choice = 0;
		do {
			logservice.InfoLogService("*************************");
			logservice.InfoLogService("Menu");
			logservice.InfoLogService("-----");
			logservice.InfoLogService("Press 1. to Log In");
			logservice.InfoLogService("Press 2. to Register");
			logservice.InfoLogService("Press 3. to Exit");
			logservice.InfoLogService("");
			logservice.InfoLogService("*v0.0.1*");
			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				choice = 0;
			}
			switch (choice) {
			case 1:				
				logservice.InfoLogService("Enter eMail");
				person.setEmail(scanner.nextLine());
				logservice.InfoLogService("Enter Password");
				person.setPsswd(scanner.nextLine());
				try {
					searchservice.verifyCredentials(person.getEmail(), person.getPsswd());
				} catch (BusinessException e) {
					logservice.WarnLogService(e.getMessage());
					break;
				}
				if (PersonValidations.isValidEmployeeEmail(person.getEmail())) {
					EmployeeSystemAccess(person);
					break;
				}
				if (PersonValidations.isValidPersonEmail(person.getEmail())) {
					CustomerSystemAccess(person);
					break;
				}
				break;
			case 2:
				customerservice.CustomerAccessRegisterNewLogin();
				break;
			case 3:
				logservice.InfoLogService("GOODBYE!");
				break;
			default:
				logservice.InfoLogService("Invalid Choice...Please enter a choice between 1-3 only.");
				break;
			}
		} while (choice != 3);
		scanner.close();
	}

	@Override
	public void CustomerSystemAccess(Person person) {

		int customerLogin = 0;
		
		try {
			customerLogin = searchservice.verifyCredentials(person.getEmail(), person.getPsswd());
		} catch (BusinessException e) {
			logservice.WarnLogService(e.getMessage());
		}
		if (customerLogin != 0) {		
			logservice.InfoLogService("=============================================================================================================================================================================");
			int option = 0;
			do {
				logservice.InfoLogService("=============================================================================================================================================================================");
				logservice.InfoLogService("MAIN MENU");
				logservice.InfoLogService("----------");
				logservice.InfoLogService("1. View account details");
				logservice.InfoLogService("2. Apply for a new account");
				logservice.InfoLogService("3. Deposit");
				logservice.InfoLogService("4. Withdrawal");
				logservice.InfoLogService("5. Post/Receive money");
				logservice.InfoLogService("6. Log out");
				try {
					option = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException | NoSuchElementException e) {

				}
				switch (option) {
				case 1:
					customerservice.CustomerAccessAccountDetails(person.getEmail());
					break;
				case 2:
					try {
						customerservice.CustomerAccessNewAccountApplication(person.getEmail());
					} catch (BusinessException e) {
						logservice.WarnLogService(e.getMessage());
					}
					break;
				case 3:
					customerservice.CustomerAccessDeposit(person.getEmail());
					break;
				case 4:
					customerservice.CustomerAccessWithdrawal(person.getEmail());
					break;
				case 5:
					customerservice.CustomerAccessTransferMoney(person.getEmail());
					break;
				case 6:
					return;
				default:
					logservice.InfoLogService("Invalid Choice...Please enter a choice between 1-6 only.");
					break;
				}
			} while (option != 6);
		}
	}

	@Override
	public void EmployeeSystemAccess(Person person) throws BusinessException {

		logservice.InfoLogService("=============================================================================================================================================================================");
		logservice.InfoLogService(":::EMPLOYEE ACCESS GRANTED:::");		
		int option = 0;
		
		do {
			option = 0;
			logservice.InfoLogService("=============================================================================================================================================================================");
			logservice.InfoLogService("MAIN MENU");
			logservice.InfoLogService("----------");
			logservice.InfoLogService("1. Search by account number");
			logservice.InfoLogService("2. List all accounts");
			logservice.InfoLogService("3. Approve/Deny pending accounts");
			logservice.InfoLogService("4. View log of all transactions");
			logservice.InfoLogService("5. Deposit to account");
			logservice.InfoLogService("6. Withdrawal from account");
			logservice.InfoLogService("7. Apply for new account");
			logservice.InfoLogService("8. Log out");
			try {
				option = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {

			}
			switch (option) {
			case 1:
				employeeservice.EmployeeAccessSearch();
				break;
			case 2:
				employeeservice.EmployeeAccessAccountList();
				break;
			case 3:
				employeeservice.EmployeeAccessApproveAccounts();
				break;
			case 4:
				employeeservice.EmployeeAccessTransactionsList();
				break;
			case 5:
				employeeservice.EmmployeeAccessDeposit();
				break;
			case 6:
				employeeservice.EmployeeAccessWithdrawal();
				break;
			case 7:
				employeeservice.EmployeeAccessAccountApplication();
				break;
			case 8:
				return;
			default:
				logservice.InfoLogService("Invalid Choice...Please enter a choice between 1-8 only.");
				break;
			}
		} while (option != 8);
	}
}