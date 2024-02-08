package com.app.bank.model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transactions {

	DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private String date = LocalDateTime.now().format(df).toString();
	DecimalFormat d = new DecimalFormat("'$'#.00");
	private double amount;
	private String transactiontype;
	private String transactionstatus;
	private int accountnumber;
	private Account account;

	public Transactions() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Transactions(int transferid, double amount, String transactiontype, String transactionstatus, String date, Account account,
			int accountnumber) {
		super();
		this.amount = amount;
		this.transactiontype = transactiontype;
		this.transactionstatus = transactionstatus;
		this.date = date;
		this.accountnumber = accountnumber;
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getTransactiontype(String transactiontype) {
		return transactiontype;
	}

	public String getTransactiontype() {
		return transactiontype;
	}

	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}

	public String getTransactionstatus() {
		return transactionstatus;
	}

	public void setTransactionstatus(String transactionstatus) {
		this.transactionstatus = transactionstatus;
	}

	public String getDate() {
		return date;
	}

	public String setDate(String date) {
		return this.date = date;
	}

	public int getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(int accountnumber) {
		this.accountnumber = accountnumber;
	}

	@Override
	public String toString() {
		return "Transaction [amount=" + d.format(amount) + ", type=" + transactiontype + ", status=" + transactionstatus
				+ ", date=" + date + ", account number=" + accountnumber + "]\n";
	}
}