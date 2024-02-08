package com.app.bank.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.app.bank.dao.AccountSearchServiceDAO;
import com.app.bank.dbutil.PostgresConnection;
import com.app.bank.exception.BusinessException;
import com.app.bank.model.Account;
import com.app.bank.model.Person;
import com.app.bank.model.Transactions;
import com.app.bank.service.LogService;

public class AccountSearchServiceDAOImpl implements AccountSearchServiceDAO {

	LogService logservice = new LogService();

	@Override
	public int verifyCredentials(String email, String psswd) throws BusinessException {

		int result = 0;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select from java_banking.person where personemail=? and personpassword=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, psswd);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				result = 1;
				return result;
			} else {
				logservice.InfoLogService("Login was unsuccessful, please try again");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
		}

		return result;
	}

	@Override
	public int getAccountByEmail(String email) throws BusinessException {

		int result = 0;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select personemail from java_banking.person where personemail=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				result = 1;
				return result;
			} else {
				logservice.InfoLogService("Customer email " + email + " was not found in the system");
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
		}

		return result;
	}

	@Override
	public List<Account> getAllAccounts() throws BusinessException {
		
		List<Account> accountList = new ArrayList<>();
		
		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select p.personname, p.personemail, a.accountnumber , a.accounttype, a.balance, a.openingdate, a.accountstatus from java_banking.person p "
					+ "join java_banking.accounts a on p.personemail =a.personemail";
			PreparedStatement preparedStatment = connection.prepareStatement(sql);

			ResultSet resultSet = preparedStatment.executeQuery();
			while (resultSet.next()) {
				Account account = new Account();
				Person person = new Person();
				person.setPersonName(resultSet.getString("personname"));
				person.setEmail(resultSet.getString("personemail"));
				account.setAccountnumber(resultSet.getInt("accountnumber"));
				account.setAccounttype(resultSet.getString("accounttype"));
				account.setBalance(resultSet.getDouble("balance"));
				account.setOpeningdate(resultSet.getString("openingdate"));
				account.setAccountStatus(resultSet.getString("accountstatus"));
				account.setPerson(person);

				accountList.add(account);
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
	public Account EmployeeAccessAccountDetails(int accountnumber) throws BusinessException {

		Account account = null;
		Person person = null;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select p.personname, p.personemail, a.accountnumber, a.accounttype, a.balance, a.openingdate, a.accountstatus from java_banking.person p "
					+ "join java_banking.accounts a on p.personemail =a.personemail where a.accountnumber=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, accountnumber);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				account = new Account();
				person = new Person();
				person.setPersonName(resultSet.getString("personname"));
				person.setEmail(resultSet.getString("personemail"));
				account.setAccountnumber(resultSet.getInt("accountnumber"));
				account.setAccounttype(resultSet.getString("accounttype"));
				account.setBalance(resultSet.getDouble("balance"));
				account.setOpeningdate(resultSet.getString("openingdate"));
				account.setAccountStatus(resultSet.getString("accountstatus"));
				account.setPerson(person);
			}
			if (account == null) {
				logservice.InfoLogService("Entered account is not registered");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
		}

		return account;
	}

	@Override
	public Account CustomerAccessAccountDetails(int accountnumber, String email) throws BusinessException {

		Account account = null;
		Person person = null;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select p.personname, p.personemail, a.accountnumber , a.accounttype, a.balance, a.openingdate, a.accountstatus from java_banking.person p "
					+ "join java_banking.accounts a on p.personemail =a.personemail where a.accountnumber=? and p.personemail=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, accountnumber);
			preparedStatement.setString(2, email);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				account = new Account();
				person = new Person();
				person.setPersonName(resultSet.getString("personname"));
				person.setEmail(resultSet.getString("personemail"));
				account.setAccountnumber(resultSet.getInt("accountnumber"));
				account.setAccounttype(resultSet.getString("accounttype"));
				account.setBalance(resultSet.getDouble("balance"));
				account.setOpeningdate(resultSet.getString("openingdate"));
				account.setAccountStatus(resultSet.getString("accountstatus"));
				account.setPerson(person);
			}
			if (account == null) {
				logservice.InfoLogService("Account not found");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return account;
	}

	@Override
	public List<Account> getAllPendingAccounts() throws BusinessException {

		List<Account> pendingAccountsList = new ArrayList<>();
		Account account = null;
		Person person = null;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select p.personname, p.personemail, a.accountnumber, a.accounttype, a.balance, a.openingdate, a.accountstatus from java_banking.person p "
					+ "join java_banking.accounts a on p.personemail= a.personemail where a.accountstatus =?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "pending");

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				account = new Account();
				person = new Person();
				account.setBalance(resultSet.getDouble("balance"));
				person.setPersonName(resultSet.getString("personname"));
				person.setEmail(resultSet.getString("personemail"));
				account.setAccountnumber(resultSet.getInt("accountnumber"));
				account.setAccounttype(resultSet.getString("accounttype"));
				account.setOpeningdate(resultSet.getString("openingdate"));
				account.setPerson(person);
				pendingAccountsList.add(account);
			}
			if (pendingAccountsList.size() == 0) {
				logservice.InfoLogService("No pending accounts found");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return pendingAccountsList;
	}

	@Override
	public List<Transactions> getAllTransactions() throws BusinessException {

		List<Transactions> allTransactions = new ArrayList<>();
		Transactions transaction = null;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select transactiontype, amount, accountnumber, transactionstatus, transactiondate from java_banking.transactions order by transactionstatus desc, transactiondate";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				transaction = new Transactions();
				transaction.setAccountnumber(resultSet.getInt("accountnumber"));
				transaction.setTransactiontype(resultSet.getString("transactiontype"));
				transaction.setAmount(resultSet.getDouble("amount"));
				transaction.setAccountnumber(resultSet.getInt("accountnumber"));
				transaction.setTransactionstatus(resultSet.getString("transactionstatus"));
				transaction.setDate(resultSet.getString("transactiondate"));
				allTransactions.add(transaction);
			}
			if (allTransactions.size() == 0) {
				logservice.InfoLogService("There are no transactions registered");
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return allTransactions;
	}

	@Override
	public List<Transactions> getTransfersByAccountNumber(int accountnumber) throws BusinessException {

		List<Transactions> accountTransfers = new ArrayList<>();
		Transactions transfer = null;

		try (Connection connection = PostgresConnection.getConnection()) {
			String sql = "select transactiontype, amount, accountnumber, transactionstatus, transactiondate from java_banking.transactions where accountnumber =? order by transactionstatus desc, transactiondate";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, accountnumber);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				transfer = new Transactions();
				transfer.setAccountnumber(resultSet.getInt("accountnumber"));
				transfer.setTransactiontype(resultSet.getString("transactiontype"));
				transfer.setAmount(resultSet.getDouble("amount"));
				transfer.setAccountnumber(resultSet.getInt("accountnumber"));
				transfer.setTransactionstatus(resultSet.getString("transactionstatus"));
				transfer.setDate(resultSet.getString("transactiondate"));
				accountTransfers.add(transfer);
			}
			if (accountTransfers.size() == 0) {
				logservice.InfoLogService("No transfers found for account number " + accountnumber);
				throw new BusinessException();
			}
		} catch (ClassNotFoundException | SQLException e) {
			logservice.WarnLogService(e.getMessage());
			throw new BusinessException();
		}

		return accountTransfers;
	}
}