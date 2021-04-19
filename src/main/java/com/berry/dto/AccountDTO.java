package com.berry.dto;

public class AccountDTO {
	private int acc_num;
	private double balance;
	private String type;	
	
	public AccountDTO() {
	}

	public AccountDTO(int acc_num, double balance, String type) {
		this.acc_num = acc_num;
		this.balance = balance;
		this.type = type;
	}
	
	public boolean noFieldEmpty() {
		if (this.type == null) {
			return false;
		}
		else if (this.type.equals("")) {
			return false;
		}
		return true;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
