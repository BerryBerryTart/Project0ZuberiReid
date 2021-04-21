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
	
	public Client() {}
	
	public Client(String fName, String lName, int id) {
		this.id = id;
		this.fName = fName;
		this.lName = lName;
	}
	
	public Client(String fName, String lName) {
		this.fName = fName;
		this.lName = lName;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accounts == null) ? 0 : accounts.hashCode());
		result = prime * result + ((fName == null) ? 0 : fName.hashCode());
		result = prime * result + id;
		result = prime * result + ((joined == null) ? 0 : joined.hashCode());
		result = prime * result + ((lName == null) ? 0 : lName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (accounts == null) {
			if (other.accounts != null)
				return false;
		} else if (!accounts.equals(other.accounts))
			return false;
		if (fName == null) {
			if (other.fName != null)
				return false;
		} else if (!fName.equals(other.fName))
			return false;
		if (id != other.id)
			return false;
		if (joined == null) {
			if (other.joined != null)
				return false;
		} else if (!joined.equals(other.joined))
			return false;
		if (lName == null) {
			if (other.lName != null)
				return false;
		} else if (!lName.equals(other.lName))
			return false;
		return true;
	}
	
	
	
}