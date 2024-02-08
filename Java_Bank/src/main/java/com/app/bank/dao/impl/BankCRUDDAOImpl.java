package com.app.bank.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.app.bank.dao.BankCRUDDAO;
import com.app.bank.dbutil.PostgresConnection;
import com.app.bank.exception.BusinessException;
import com.app.bank.model.Account;
import com.app.bank.model.Person;
import com.app.bank.model.Transactions;
import com.app.bank.service.LogService;
import com.app.bank.service.impl.PersonValidations;

public class BankCRUDDAOImpl implements BankCRUDDAO {

	LogService logservice = new LogService();
	Scanner scanner = new Scanner(System.in);

	@Override
	public int createPerson(Person newPerson) throws BusinessException {

		int result = 0;
		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select personemail from java_banking.person where personemail =?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, newPerson.getEmail());

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				logservice.InfoLogService("Account exists, please try again");
				throw new BusinessException();
			} else {
				sql = "insert into java_banking.person(personname,personemail,personpassword) values(?,?,?)";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, newPerson.getPersonName());
				preparedStatement.setString(2, newPerson.getEmail());
				preparedStatement.setString(3, newPerson.getPsswd());
				result = preparedStatement.executeUpdate();
			}
			if (result == 0) {
				logservice.WarnLogService("Internal error");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return result;
	}

	@Override
	public List<Account> EmployeeAccessAccountApplication(Account account) throws BusinessException {

		int result = 0;
		List<Account> accountList = new ArrayList<>();
		Account newAccounts = null;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select personemail from java_banking.person where personemail =?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, account.getPersonemail());

			ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				logservice.InfoLogService("Account not found");
				throw new BusinessException();
			} else {
				sql = "insert into java_banking.accounts(balance, personemail, openingdate, accounttype, accountstatus) values(?,?,?,?,?)";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setDouble(1, account.getBalance());
				preparedStatement.setString(2, account.getPersonemail());
				preparedStatement.setString(3, account.getDate());
				preparedStatement.setString(4, account.getAccounttype());
				preparedStatement.setString(5, "pending");
				result = preparedStatement.executeUpdate();

				sql = "select accountnumber, accounttype, openingdate, balance, accountstatus from java_banking.accounts where personemail=? and accountstatus=?";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, account.getPersonemail());
				preparedStatement.setString(2, account.getAccountStatus());

				ResultSet resultSet2 = preparedStatement.executeQuery();
				while (resultSet2.next()) {
					newAccounts = new Account();
					newAccounts.setAccountnumber(resultSet2.getInt("accountNumber"));
					newAccounts.setAccounttype(resultSet2.getString("accounttype"));
					newAccounts.setBalance(resultSet2.getDouble("balance"));
					newAccounts.setOpeningdate(resultSet2.getString("openingdate"));
					newAccounts.setAccountStatus(resultSet2.getString("accountstatus"));
					accountList.add(newAccounts);
				}
				if (accountList.size() == 0 || result == 0) {
					logservice.WarnLogService("Internal error");
					throw new BusinessException();
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return accountList;
	}

	@Override
	public int deletePersonbyEmail(String email) throws BusinessException {

		int result = 0;
		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "delete from java_banking.person where personemail =?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);

			result = preparedStatement.executeUpdate();
			if (result == 0) {
				logservice.WarnLogService("Internal error");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return result;
	}

	@Override
	public Transactions CustomerAccessDeposit(Transactions deposit, String email) throws BusinessException {

		double newBalance = 0;
		int update1 = 0;
		int update2 = 0;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select a.accountnumber, a.accountstatus, a.balance from java_banking.person p "
					+ "join java_banking.accounts a on p.personemail =a.personemail where a.accountnumber=? and p.personemail=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, deposit.getAccountnumber());
			preparedStatement.setString(2, email);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				newBalance = resultSet.getDouble("balance") + deposit.getAmount();
				if (!PersonValidations.isValidAccountStatus(resultSet.getString("accountstatus"))) {
					logservice.InfoLogService("Account is not approved, transaction denied");
					throw new BusinessException();
				}
			}

			else {
				logservice.InfoLogService("Account not found");
				throw new BusinessException();
			}

			sql = "update java_banking.accounts set balance =? where accountnumber =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, newBalance);
			preparedStatement.setInt(2, deposit.getAccountnumber());

			update1 = preparedStatement.executeUpdate();

			sql = "select p.personname, p.personemail, a.accountnumber, a.accounttype, a.balance, a.openingdate, a.accountstatus from java_banking.person p "
					+ "join java_banking.accounts a on p.personemail =a.personemail where a.accountnumber=? and p.personemail=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, deposit.getAccountnumber());
			preparedStatement.setString(2, email);

			sql = "insert into java_banking.transactions(amount, transactiondate, transactiontype, transactionstatus, accountnumber) values(?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, deposit.getAmount());
			preparedStatement.setString(2, deposit.getDate());
			preparedStatement.setString(3, deposit.getTransactiontype());
			preparedStatement.setString(4, deposit.getTransactionstatus());
			preparedStatement.setInt(5, deposit.getAccountnumber());
			update2 = preparedStatement.executeUpdate();

			if (update1 == 0 || update2 == 0) {
				logservice.InfoLogService("Deposit to " + deposit.getAccountnumber() + " was unsuccessful");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return deposit;
	}

	@Override
	public Transactions EmployeeAccessDeposit(Transactions deposit) throws BusinessException {

		double newBalance = 0;
		int update1 = 0;
		int update2 = 0;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select accountnumber, accountstatus, balance from java_banking.accounts where accountnumber=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, deposit.getAccountnumber());

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				newBalance = resultSet.getDouble("balance") + deposit.getAmount();
				if (!PersonValidations.isValidAccountStatus(resultSet.getString("accountstatus"))) {
					logservice.InfoLogService("Account is not approved, transaction denied");
					throw new BusinessException();
				}
			} else {
				logservice.InfoLogService("Account not found");
				throw new BusinessException();
			}

			sql = "update java_banking.accounts set balance =? where accountnumber =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, newBalance);
			preparedStatement.setInt(2, deposit.getAccountnumber());

			update1 = preparedStatement.executeUpdate();

			sql = "select p.personname, p.personemail, a.accountnumber, a.accounttype, a.balance, a.openingdate, a.accountstatus from java_banking.person p "
					+ "join java_banking.accounts a on p.personemail =a.personemail where a.accountnumber=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, deposit.getAccountnumber());

			sql = "insert into java_banking.transactions(amount, transactiondate, transactiontype, transactionstatus, accountnumber) values(?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, deposit.getAmount());
			preparedStatement.setString(2, deposit.getDate());
			preparedStatement.setString(3, deposit.getTransactiontype());
			preparedStatement.setString(4, deposit.getTransactionstatus());
			preparedStatement.setInt(5, deposit.getAccountnumber());
			update2 = preparedStatement.executeUpdate();

			if (update1 == 0 || update2 == 0) {
				logservice.InfoLogService("Deposit to " + deposit.getAccountnumber() + " was unsuccessful");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return deposit;
	}

	@Override
	public Transactions EmployeeAccessWithdrawal(Transactions withdrawal) throws BusinessException {

		double newBalance = 0;
		int updateResult = 0;
		int updateResult2 = 0;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select accountnumber, accountstatus, balance from java_banking.accounts where accountnumber=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, withdrawal.getAccountnumber());

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				newBalance = resultSet.getDouble("balance") - withdrawal.getAmount();
				if (!PersonValidations.isValidAccountStatus(resultSet.getString("accountstatus"))) {
					logservice.InfoLogService("Account is not approved, transaction denied");
					throw new BusinessException();
				}
				if (!PersonValidations.isValidTransactionAmount(newBalance)) {
					logservice.WarnLogService("SYSTEM error: non-sufficient funds");
					throw new BusinessException();
				}
			} else {
				logservice.InfoLogService("Account not found");
				throw new BusinessException();
			}

			sql = "update java_banking.accounts set balance =? where accountnumber =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, newBalance);
			preparedStatement.setInt(2, withdrawal.getAccountnumber());
			updateResult = preparedStatement.executeUpdate();
			sql = "insert into java_banking.transactions(amount, transactiondate, transactiontype, transactionstatus, accountnumber) values(?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, withdrawal.getAmount());
			preparedStatement.setString(2, withdrawal.getDate());
			preparedStatement.setString(3, withdrawal.getTransactiontype());
			preparedStatement.setString(4, withdrawal.getTransactionstatus());
			preparedStatement.setInt(5, withdrawal.getAccountnumber());
			updateResult2 = preparedStatement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}
		if (updateResult == 0 || updateResult2 == 0) {
			logservice.InfoLogService("Withdrawal to " + withdrawal.getAccountnumber() + " was unsuccessful");
			throw new BusinessException();
		}

		return withdrawal;
	}

	@Override
	public Transactions CustomerAccessWithdrawal(Transactions withdrawal, String email) throws BusinessException {

		double newBalance = 0;
		int updateResult = 0;
		int updateResult2 = 0;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select a.accountnumber, a.accountstatus, a.balance from java_banking.person p "
					+ "join java_banking.accounts a on p.personemail =a.personemail where a.accountnumber=? and p.personemail=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, withdrawal.getAccountnumber());
			preparedStatement.setString(2, email);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				newBalance = resultSet.getDouble("balance") - withdrawal.getAmount();
				if (!PersonValidations.isValidAccountStatus(resultSet.getString("accountstatus"))) {
					logservice.InfoLogService("Account is not approved, transaction denied");
					throw new BusinessException();
				}
				if (!PersonValidations.isValidTransactionAmount(newBalance)) {
					logservice.WarnLogService("SYSTEM error: non-sufficient funds");
					throw new BusinessException();
				}
			} else {
				logservice.InfoLogService("Account not found");
				throw new BusinessException();
			}

			sql = "update java_banking.accounts set balance =? where accountnumber =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, newBalance);
			preparedStatement.setInt(2, withdrawal.getAccountnumber());
			updateResult = preparedStatement.executeUpdate();
			sql = "insert into java_banking.transactions(amount, transactiondate, transactiontype, transactionstatus, accountnumber) values(?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, withdrawal.getAmount());
			preparedStatement.setString(2, withdrawal.getDate());
			preparedStatement.setString(3, withdrawal.getTransactiontype());
			preparedStatement.setString(4, withdrawal.getTransactionstatus());
			preparedStatement.setInt(5, withdrawal.getAccountnumber());
			updateResult2 = preparedStatement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}
		if (updateResult == 0 || updateResult2 == 0) {
			logservice.InfoLogService("Withdrawal to " + withdrawal.getAccountnumber() + " was unsuccessful");
			throw new BusinessException();
		}

		return withdrawal;
	}

	@Override
	public List<Account> CustomerAccessAccountApplication(Account account, String email) throws BusinessException {

		List<Account> accountList = new ArrayList<>();
		Account newAccounts = null;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "insert into java_banking.accounts(balance, personemail, openingdate, accounttype, accountstatus) values(?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, account.getBalance());
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, account.getDate());
			preparedStatement.setString(4, account.getAccounttype());
			preparedStatement.setString(5, "pending");
			preparedStatement.executeUpdate();

			sql = "select accountnumber, accounttype, openingdate, balance, accountstatus from java_banking.accounts where personemail=? and accountstatus=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, "pending");

			ResultSet resultSet2 = preparedStatement.executeQuery();
			while (resultSet2.next()) {
				newAccounts = new Account();
				newAccounts.setAccountnumber(resultSet2.getInt("accountNumber"));
				newAccounts.setAccounttype(resultSet2.getString("accounttype"));
				newAccounts.setBalance(resultSet2.getDouble("balance"));
				newAccounts.setOpeningdate(resultSet2.getString("openingdate"));
				newAccounts.setAccountStatus(resultSet2.getString("accountstatus"));
				accountList.add(newAccounts);
			}
			if (accountList.size() == 0) {
				logservice.WarnLogService("Internal error");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return accountList;
	}

	@Override
	public int approveAccountByAccountNumber(int accountnumber) throws BusinessException {

		int updateResult = 0;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select accountnumber, accountstatus from java_banking.accounts where accountnumber=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, accountnumber);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (PersonValidations.isValidAccountStatus(resultSet.getString("accountstatus"))) {
					logservice.InfoLogService("Account " + accountnumber + " has already been approved");
					throw new BusinessException();
				}
			} else {
				logservice.InfoLogService("Account not found");
				throw new BusinessException();
			}
			sql = "update java_banking.accounts set accountstatus =? where accountnumber =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "approved");
			preparedStatement.setInt(2, accountnumber);
			updateResult = preparedStatement.executeUpdate();
			if (updateResult == 0) {
				logservice.InfoLogService("Entered account number " + accountnumber + " is not registered");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}
		return updateResult;
	}

	@Override
	public int denyAccountByAccountNumber(int accountnumber) throws BusinessException {

		int updateResult = 0;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select accountnumber, accountstatus from java_banking.accounts where accountnumber=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, accountnumber);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (!PersonValidations.isValidAccountStatus(resultSet.getString("accountstatus"))) {
					logservice.InfoLogService("Account number" + accountnumber + " has already been denied");
					throw new BusinessException();
				}
			} else {
				logservice.InfoLogService("Account not found");
				throw new BusinessException();
			}
			sql = "update java_banking.accounts set accountstatus =? where accountnumber =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "denied");
			preparedStatement.setInt(2, accountnumber);
			updateResult = preparedStatement.executeUpdate();
			if (updateResult == 0) {
				logservice.InfoLogService("Entered account number " + accountnumber + " is not registered");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return updateResult;
	}

	@Override
	public Transactions postTransfer(String email, Transactions postTransfer) throws BusinessException {

		int result1 = 0;
		int result2 = 0;
		Transactions updateAccount = null;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select a.accountnumber, a.accountstatus from java_banking.person p "
					+ "join java_banking.accounts a on p.personemail =a.personemail where a.accountnumber=? and p.personemail=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, postTransfer.getAccountnumber());
			preparedStatement.setString(2, email);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (!PersonValidations.isValidAccountStatus(resultSet.getString("accountstatus"))) {
					logservice.InfoLogService("Transfer denied, account number " + postTransfer.getAccountnumber()
							+ " has not been approved.");
					throw new BusinessException();
				}
			} else {
				logservice.InfoLogService("Account not found");
				throw new BusinessException();
			}

			sql = "select accountnumber, accountstatus from java_banking.accounts where accountnumber=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, postTransfer.getAccount().getAccountnumber());
			ResultSet resultSet1 = preparedStatement.executeQuery();
			if (resultSet1.next()) {
				if (!PersonValidations.isValidAccountStatus(resultSet1.getString("accountstatus"))) {
					logservice.InfoLogService("Transfer denied, account number "
							+ postTransfer.getAccount().getAccountnumber() + " has not been approved.");
					throw new BusinessException();
				}
			} else {
				logservice.InfoLogService("Destination account was not found");
				throw new BusinessException();
			}

			sql = "insert into java_banking.transactions (amount, transactiontype, transactionstatus, transactiondate, accountnumber) values (?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, postTransfer.getAmount());
			preparedStatement.setString(2, "post");
			preparedStatement.setString(3, "pending");
			preparedStatement.setString(4, postTransfer.getDate());
			preparedStatement.setInt(5, postTransfer.getAccountnumber());
			result1 = preparedStatement.executeUpdate();

			sql = "insert into java_banking.transactions (amount, transactiontype, transactionstatus, transactiondate, accountnumber) values (?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, postTransfer.getAmount());
			preparedStatement.setString(2, "receive");
			preparedStatement.setString(3, "pending");
			preparedStatement.setString(4, postTransfer.getDate());
			preparedStatement.setInt(5, postTransfer.getAccount().getAccountnumber());
			result2 = preparedStatement.executeUpdate();

			sql = "select accountnumber, transactionstatus, transactiondate, transactiontype, transactiondate, amount from java_banking.transactions where transactiondate =? and accountnumber =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, postTransfer.getDate());
			preparedStatement.setInt(2, postTransfer.getAccountnumber());
			ResultSet resultSet2 = preparedStatement.executeQuery();
			if (resultSet2.next()) {
				updateAccount = new Transactions();
				updateAccount.setAccountnumber(resultSet2.getInt("accountnumber"));
				updateAccount.setTransactionstatus(resultSet2.getString("transactionstatus"));
				updateAccount.setAmount(resultSet2.getDouble("amount"));
				updateAccount.setDate(resultSet2.getString("transactiondate"));
				updateAccount.setTransactiontype(resultSet2.getString("transactiontype"));
			}
			if (result1 == 0 || result2 == 0 || resultSet1 == null || resultSet2 == null) {
				logservice.WarnLogService("Internal error");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return updateAccount;
	}

	@Override
	public Transactions acceptTransfer(String email, Transactions transaction) throws BusinessException {

		int result = 0;
		double newBalance = 0;
		int updateResult = 0;
		Transactions updateAccount = null;
		Transactions withdrawalAccount = new Transactions();

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select accountnumber from java_banking.transactions where transactiondate=? and transactionstatus=? and transactiontype =?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, transaction.getDate());
			preparedStatement.setString(2, transaction.getTransactionstatus());
			preparedStatement.setString(3, "post");
			ResultSet resultSet1 = preparedStatement.executeQuery();
			if (resultSet1.next()) {
				withdrawalAccount.setAccountnumber(resultSet1.getInt("accountnumber"));
			}
			sql = "select accountnumber, accountstatus, balance from java_banking.accounts where accountnumber=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, withdrawalAccount.getAccountnumber());

			ResultSet resultSet2 = preparedStatement.executeQuery();
			if (resultSet2.next()) {
				newBalance = resultSet2.getDouble("balance") - transaction.getAmount();
				if (!PersonValidations.isValidAccountStatus(resultSet2.getString("accountstatus"))) {
					logservice.InfoLogService("Account is not approved, transaction denied");
					throw new BusinessException();
				}
				if (!PersonValidations.isValidTransactionAmount(newBalance)) {
					logservice.WarnLogService("SYSTEM error: non-sufficient funds");
					throw new BusinessException();
				}
			} else {
				logservice.InfoLogService("Account not found");
				throw new BusinessException();
			}

			sql = "update java_banking.accounts set balance =? where accountnumber =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, newBalance);
			preparedStatement.setInt(2, withdrawalAccount.getAccountnumber());
			updateResult = preparedStatement.executeUpdate();

			sql = "update java_banking.transactions set transactionstatus =? where accountnumber =? and transactiondate =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "approved");
			preparedStatement.setInt(2, withdrawalAccount.getAccountnumber());
			preparedStatement.setString(3, transaction.getDate());
			result = preparedStatement.executeUpdate();
			if (updateResult == 0 || result == 0 || resultSet1 == null || resultSet2 == null) {
				logservice.InfoLogService("Withdrawal to " + transaction.getAccountnumber() + " was unsuccessful");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		double newBalance2 = 0;
		int update1 = 0;
		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select a.accountnumber, a.accountstatus, a.balance from java_banking.person p "
					+ "join java_banking.accounts a on p.personemail =a.personemail where a.accountnumber=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, transaction.getAccountnumber());

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				if (!PersonValidations.isValidAccountStatus(resultSet.getString("accountstatus"))) {
					logservice.InfoLogService("Account is not approved, transaction denied");
					throw new BusinessException();
				}
				newBalance2 = resultSet.getDouble("balance") + transaction.getAmount();

			} else {
				logservice.InfoLogService("Account not found");
				throw new BusinessException();
			}

			sql = "update java_banking.accounts set balance =? where accountnumber =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, newBalance2);
			preparedStatement.setInt(2, transaction.getAccountnumber());
			update1 = preparedStatement.executeUpdate();

			sql = "update java_banking.transactions set transactionstatus =? where transactiondate =? and transactiontype =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "approved");
			preparedStatement.setString(2, transaction.getDate());
			preparedStatement.setString(3, "receive");
			result = preparedStatement.executeUpdate();

			sql = "select accountnumber, transactionstatus, transactiondate, transactiontype, transactiondate, amount from transactions where transactiondate =? and accountnumber =?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, transaction.getDate());
			preparedStatement.setInt(2, transaction.getAccountnumber());
			ResultSet resultSet2 = preparedStatement.executeQuery();
			if (resultSet2.next()) {
				updateAccount = new Transactions();
				updateAccount.setAccountnumber(resultSet2.getInt("accountnumber"));
				updateAccount.setTransactionstatus(resultSet2.getString("transactionstatus"));
				updateAccount.setAmount(resultSet2.getDouble("amount"));
				updateAccount.setDate(resultSet2.getString("transactiondate"));
				updateAccount.setTransactiontype(resultSet2.getString("transactiontype"));
			}
			if (update1 == 0 || resultSet2 == null) {
				logservice.InfoLogService("Deposit to " + transaction.getAccountnumber() + " was unsuccessful");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		} catch (BusinessException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return updateAccount;
	}
}