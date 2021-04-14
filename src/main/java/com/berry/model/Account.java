package com.berry.model;

public class Account {
	private int id;
	private int fk;
	private String type;
	private long acc_num;
	private double balance;
	
	public Account(int id, int fk, String type, long acc_num, double balance) {
		this.id = id;
		this.fk = fk;
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

	public int getFk() {
		return fk;
	}

	public void setFk(int fk) {
		this.fk = fk;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getAcc_num() {
		return acc_num;
	}

	public void setAcc_num(long acc_num) {
		this.acc_num = acc_num;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
