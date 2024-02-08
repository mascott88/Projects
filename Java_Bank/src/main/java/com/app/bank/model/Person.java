package com.app.bank.model;

public class Person {

	private String personname;
	private String email;
	private String psswd;

	public Person() {
		super();

	}

	public Person( String personname, String email, String password) {
		super();
		this.personname = personname;
		this.email = email;
		this.psswd = password;
	}

	public String getPersonName() {
		return personname;
	}

	public void setPersonName(String personname) {
		this.personname = personname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPsswd() {
		return psswd;
	}

	public void setPsswd(String psswd) {
		this.psswd = psswd;
	}

	@Override
	public String toString() {
		return "Person [ Name=" + personname + ", eMail=" + email+"]\n";
	}
}
