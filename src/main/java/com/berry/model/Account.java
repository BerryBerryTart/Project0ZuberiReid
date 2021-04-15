package com.berry.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Account {
	private int id;
	private String type;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String created;
	private int acc_num;
	private double balance;
	
	//fully qualified account object
	public Account(int id, String type, String created, int acc_num, double balance) {
		this.id = id;
		this.type = type;
		this.created = created;
		this.acc_num = acc_num;
		this.balance = balance;
	}

	//only returned on creation. ommited creation date to keep function pure
	public Account(int id, String type, int acc_num, double balance) {
		super();
		this.id = id;
		this.type = type;
		this.acc_num = acc_num;
		this.balance = balance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public int getAcc_num() {
		return acc_num;
	}

	public void setAcc_num(int acc_num) {
		this.acc_num = acc_num;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
