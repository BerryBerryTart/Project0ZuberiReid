package com.berry.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Client {
	private int id;
	private String fName;
	private String lName;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String joined;	
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private ArrayList<Account> accounts;
	
	//full qualified client 
	public Client(String fName, String lName, String joined, int id, ArrayList<Account> accounts) {
		this(fName, lName, joined, id);
		this.accounts = accounts;
	}	
	
	//for fetching client data
	public Client(String fName, String lName, String joined, int id) {
		this.fName = fName;
		this.lName = lName;
		this.joined = joined;
		this.id = id;
	}
	
	//ONLY returned when creating a client. Ommited creation date to keep function pure
	public Client(String fName, String lName, int id) {
		this.fName = fName;
		this.lName = lName;
		this.id = id;
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