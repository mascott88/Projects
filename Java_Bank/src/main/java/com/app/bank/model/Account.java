package com.app.bank.model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Account {

	DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private String date = LocalDateTime.now().format(df).toString();
	DecimalFormat d = new DecimalFormat("'$'#.00");
	private int accountnumber;
	private String accounttype;
	private double balance;
	private String openingdate;
	private Person person;
	private String accountStatus;
	private String personemail;

	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Account(int accountnumber, String accounttype, double balance, String openingdate, Person person,String date, String accountStatus, String personemail) {
		super();
		this.accountnumber = accountnumber;
		this.accounttype = accounttype;
		this.balance = balance;
		this.openingdate = openingdate;
		this.person = person;
		this.date = date;
		this.accountStatus = accountStatus;
		this.personemail = personemail;
	}

	public String getPersonemail() {
		return personemail;
	}

	public void setPersonemail(String personemail) {
		this.personemail = personemail;
	}

	public String getDate() {
		return date;
	}

	public String setDate(String date) {
		return this.date = date;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(int accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getOpeningdate() {
		return openingdate;
	}

	public void setOpeningdate(String openingdate) {
		this.openingdate = openingdate;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String toString() {
		return "[Account#=" + accountnumber + ", type=" + accounttype + ", balance=" + d.format(balance) + ", opening date="
				+ openingdate + ", status=" + accountStatus + ", name=" + person.getPersonName() + ", eMail address="
				+ person.getEmail() + "]\n";
	}	
}
