package com.berry.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Client {
	private int id;
	private String fName;
	private String lName;
	private String joined;	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ArrayList<Account> accounts;
	
	public Client(String fName, String lName, String joined, ArrayList<Account> accounts) {
		this(fName, lName, joined);
		this.accounts = accounts;
	}	
	
	public Client(String fName, String lName, String joined) {
		this.fName = fName;
		this.lName = lName;
		this.joined = joined;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getJoined() {
		return joined;
	}

	public void setJoined(String joined) {
		this.joined = joined;
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
}