package com.app.bank.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.app.bank.exception.BusinessException;
import com.app.bank.model.Account;
import com.app.bank.model.Person;
import com.app.bank.model.Transactions;
import com.app.bank.service.EmployeeAccountService;
import com.app.bank.service.AccountSearchService;
import com.app.bank.service.BankCRUDService;
import com.app.bank.service.CustomerAccountService;
import com.app.bank.service.LogService;

public class CustomerAccessServiceImpl implements CustomerAccountService {

	BankCRUDService bankcrudservice = new BankCRUDServiceImpl();
	AccountSearchService searchservice = new AccountSearchServiceImpl();
	EmployeeAccountService accountservice = new EmployeeAccessServiceImpl();
	LogService logservice = new LogService();
	Scanner scanner = new Scanner(System.in);
	DecimalFormat d = new DecimalFormat("'$'#.00");

	public Transactions CustomerAccessDeposit(String loginEmail) {

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
			depositResult = bankcrudservice.CustomerAccessDeposit(depositResult, loginEmail);
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

	public Transactions CustomerAccessWithdrawal(String loginEmail) {

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
			withdrawalResult = bankcrudservice.CustomerAccessWithdrawal(withdrawalResult, loginEmail);
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
	public Account CustomerAccessAccountDetails(String loginEmail) {

		Account customerViewResult = null;
		int customerViewAccount = 0;

		try {
			logservice.InfoLogService("Enter Account number");
			customerViewAccount = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			logservice.InfoLogService("Entered account number is Invalid");
			return null;
		}
		try {
			customerViewResult = searchservice.CustomerAccessAccountDetails(customerViewAccount, loginEmail);
			if (customerViewResult != null) {
				logservice.InfoLogService(customerViewResult.toString());
				return customerViewResult;
			}
		} catch (BusinessException e) {
			logservice.WarnLogService(e.getMessage());
			return null;
		}

		return null;
	}

	public Transactions CustomerAccessTransferMoney(String loginEmail) {

		int option = 0;

		do {
			logservice.InfoLogService("*****************************************************************************************************************************************************************************");
			logservice.InfoLogService("Transfer Money");
			logservice.InfoLogService("*****************************************************************************************************************************************************************************");
			logservice.InfoLogService("1. Post money transfer");
			logservice.InfoLogService("2. Accept money transfer");
			logservice.InfoLogService("3. Main Menu");
			try {
				option = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
			}
			switch (option) {
			case 1:
				Transactions transferPost = new Transactions();
				Account receiveAccount = new Account();
				transferPost.setAccount(receiveAccount);
				do {
					logservice.InfoLogService("*****************************************************************************************************************************************************************************");
					logservice.InfoLogService("Post Transfer Menu");
					logservice.InfoLogService("*****************************************************************************************************************************************************************************");
					logservice.InfoLogService("1. Send money");
					logservice.InfoLogService("2. View pending post transfers");
					logservice.InfoLogService("3. Go back to main menu");
					try {
						option = Integer.parseInt(scanner.nextLine());
					} catch (NumberFormatException e) {
					}
					switch (option) {
					case 1:
						try {
							logservice.InfoLogService("Enter account number to send money from");
							transferPost.setAccountnumber(Integer.parseInt(scanner.nextLine()));
						} catch (NumberFormatException e) {
							logservice.InfoLogService("Entered account number is Invalid");
							break;
						}
						try {
							logservice.InfoLogService("Enter destination account number");
							transferPost.getAccount().setAccountnumber(Integer.parseInt(scanner.nextLine()));
							if (transferPost.getAccountnumber() == transferPost.getAccount().getAccountnumber()) {
								logservice
										.InfoLogService("Destination account cannot be the same as the origin account");
								break;
							}
						} catch (NumberFormatException e) {
							logservice.InfoLogService("Entered account number is Invalid");
							break;
						}
						try {
							logservice.InfoLogService("Enter amount to be transferred");
							transferPost.setAmount(Integer.parseInt(scanner.nextLine()));
						} catch (NumberFormatException e) {
							logservice.InfoLogService("Entered amount is Invalid");
							break;
						}
						try {
							Transactions post = bankcrudservice.postTransfer(loginEmail, transferPost);
							if (post != null) {
								logservice.InfoLogService("Posted amount of " + d.format(transferPost.getAmount())
										+ " from account number " + transferPost.getAccountnumber() + " to account number "
										+ transferPost.getAccount().getAccountnumber() + " is pending");
								logservice.InfoLogService(post.toString());
								return post;
							}
						} catch (BusinessException e) {
							logservice.InfoLogService(e.getMessage());
							break;
						}
					case 2:
						Account currentAccount = new Account();
						List<Transactions> pendingAccounts = new ArrayList<>();
						try {
							logservice.InfoLogService("Enter account number");
							int inputAccount = Integer.parseInt(scanner.nextLine());
							searchservice.getAllAccounts().stream()
									.filter(x -> x.getPerson().getEmail().equals(loginEmail))
									.filter(x -> x.getAccountnumber() == (inputAccount))
									.forEach(x -> currentAccount.setAccountnumber(x.getAccountnumber()));
							if (currentAccount.getAccountnumber() == 0) {
								logservice.InfoLogService("Account not found");
								break;
							}
							searchservice.getTransfersByAccountNumber(inputAccount).stream()
									.filter(x -> x.getTransactionstatus().equals("pending"))
									.filter(x -> x.getTransactiontype().equals("post"))
									.forEach(x -> pendingAccounts.add(x));
							if (pendingAccounts.size() != 0) {
								for (Transactions x : pendingAccounts) {
									logservice.InfoLogService(x);
								}
							} else {
								logservice.InfoLogService(
										"Account number " + inputAccount + " has no pending post transfers");
							}
						} catch (BusinessException e) {
							logservice.InfoLogService(e.getMessage());
							break;
						} catch (NumberFormatException e) {
							logservice.InfoLogService("Entered account number is Invalid");
							break;
						}
						break;
					case 3:
						break;
					default:
						logservice.InfoLogService("Invalid Choice...Please enter a choice between 1-3 only.");
						option = 0;
						break;
					}
				} while (option != 3);
				break;
			case 2:
				try {
					List<Transactions> finalAcceptList = new ArrayList<>();
					int choice = 0;
					do {
						List<Integer> accountList = new ArrayList<>();
						List<Transactions> acceptList = new ArrayList<>();
						Transactions transferResult = null;
						searchservice.getAllAccounts().stream().filter(x -> x.getPerson().getEmail().equals(loginEmail))
								.forEach(x -> accountList.add(x.getAccountnumber()));
						searchservice.getAllTransactions().stream()
								.filter(x -> x.getTransactiontype().equals("receive"))
								.filter(x -> accountList.contains(x.getAccountnumber()))
								.filter(x -> x.getTransactionstatus().equals("pending"))
								.forEach(x -> acceptList.add(x));
						finalAcceptList = acceptList;
						if (acceptList.size() != 0) {
							logservice.InfoLogService("Please select the transaction to accept from the list below");
							logservice.InfoLogService("*****************************************************************************************************************************************************************************");
							for (int i = 0; i < acceptList.size(); i++) {
								logservice.InfoLogService((i + 1) + ")" + acceptList.get(i));
							}
							logservice.InfoLogService(acceptList.size() + 1 + ")Go back to main menu");
							try {
								logservice.InfoLogService("*****************************************************************************************************************************************************************************");
								logservice.InfoLogService("Please enter choice from 1-" + (acceptList.size() + 1)
										+ " to accept a transaction");
								choice = Integer.parseInt(scanner.nextLine());
								if (choice > 0 && choice <= acceptList.size() + 1) {
									if (choice == acceptList.size() + 1) {
										return null;
									} else {
										transferResult = bankcrudservice.acceptTransfer(loginEmail, acceptList.get(choice-1));
										if (transferResult != null) {
											logservice.InfoLogService("Transfer successful, " + d.format(acceptList.get(choice-1).getAmount()) + " was received");
											logservice.InfoLogService(transferResult.toString());
											break;
										}
									}
								} else {																		
									logservice.InfoLogService("-------------------------------------------------------------------------------------------------------------------------------------------------------");
									logservice.InfoLogService("Invalid choice");
									logservice.InfoLogService("-------------------------------------------------------------------------------------------------------------------------------------------------------");
								}
							} catch (NumberFormatException e) {
								logservice.InfoLogService("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
								logservice.InfoLogService("Choice should be number only, please choose again");
								logservice.InfoLogService("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
							}
						} else {							
							logservice.InfoLogService("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
							logservice.InfoLogService("There are no pending transfers to receive");
							logservice.InfoLogService("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
							break;
						}
					} while (choice != finalAcceptList.size() + 1);
				} catch (BusinessException e) {
					logservice.InfoLogService(e.getMessage());
					break;
				}
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

	private List<Account> apply(String accountType, String loginEmail) {

		Account account = new Account();
		double amount = 0.0;
		account.setAccounttype(accountType);
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
			List<Account> newAccount = bankcrudservice.CustomerAccessAccountApplication(account, loginEmail);
			if (newAccount != null) {
				logservice.InfoLogService("*****************************************************************************************************************************************************************************");
				logservice.InfoLogService("Account registration successful, Welcome to ROC Banking.");
				logservice.InfoLogService("*****************************************************************************************************************************************************************************");
				logservice.InfoLogService("New " + account.getAccounttype()
						+ " account application received. Listing your pending accounts below...");
				for (Account a : newAccount)
					logservice.InfoLogService("Account number: " + a.getAccountnumber() + ", opening date: "
							+ a.getOpeningdate() + ", balance: " + d.format(a.getBalance()) + ", status: "
							+ a.getAccountStatus() + "\n");
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
	public List<Account> CustomerAccessNewAccountApplication(String loginEmail) {

		String accountType = null;
		int option = 0;

		do {
			logservice.InfoLogService("*****************************************************************************************************************************************************************************");
			logservice.InfoLogService("ROC Bank account registration");
			logservice.InfoLogService("*****************************************************************************************************************************************************************************");
			logservice.InfoLogService("Enter choice for account type");
			logservice.InfoLogService("1. Apply for checking account");
			logservice.InfoLogService("2. Apply for savings account");
			logservice.InfoLogService("3. Not now");
			try {
				option = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				option = 0;
			}
			switch (option) {
			case 1:
				accountType = "checking";
				return apply(accountType, loginEmail);
			case 2:
				accountType = "savings";
				return apply(accountType, loginEmail);
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

	@Override
	public List<Account> CustomerAccessRegisterNewLogin() {

		Person registerForLogin = new Person();

		logservice.InfoLogService("*****************************************************************************************************************************************************************************");
		logservice.InfoLogService("Register for a new ROC account ");
		logservice.InfoLogService("*****************************************************************************************************************************************************************************");
		logservice.InfoLogService("Enter Person name");
		registerForLogin.setPersonName(scanner.nextLine());
		logservice.InfoLogService("Enter eMail address");
		registerForLogin.setEmail(scanner.nextLine());
		logservice.InfoLogService("Enter password");
		registerForLogin.setPsswd(scanner.nextLine());
		try {
			int result = bankcrudservice.createPerson(registerForLogin);
			if (result != 0) {
				logservice.InfoLogService("*****************************************************************************************************************************************************************************");
				logservice.InfoLogService("Login registration was successful");
				logservice.InfoLogService("*****************************************************************************************************************************************************************************");				
				CustomerAccessNewAccountApplication(registerForLogin.getEmail());
			}
		} catch (BusinessException e) {
			logservice.WarnLogService(e.getMessage());
			return null;
		}

		return null;
	}
}