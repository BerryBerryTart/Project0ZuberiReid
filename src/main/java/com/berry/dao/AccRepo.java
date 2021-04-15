package com.berry.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.berry.model.Account;

public class AccRepo {
	public AccRepo() {
		super();
	}
	
	
	private Account getAccountFromRS(ResultSet rs) throws SQLException{
		Account acc = null;
		int id = rs.getInt("id");
		String type = rs.getString("type");
		String created = rs.getString("created");
		int acc_num = rs.getInt("acc_num");
		double balance = rs.getDouble("balance");
		acc = new Account(id, type, created, acc_num, balance);
		return acc;
	}
}
