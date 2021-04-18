package com.berry.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berry.app.Application;
import com.berry.exception.DatabaseException;
import com.berry.exception.NotFoundException;
import com.berry.model.Account;
import com.berry.model.Client;

public class AccountRepo{
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Statement stmt = null;
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public AccountRepo() {
		super();
	}
	
	public ArrayList<Account> getAllAccounts(int id) throws DatabaseException, NotFoundException {
		ArrayList<Account> accs = new ArrayList<Account>();
		
		try {
			conn = ConnectionUtil.connectToDB();
			if (fetchClient(id) == null) {
				throw new NotFoundException("No Client Found");
			}
			String accSQL = "SELECT * FROM clients.account WHERE fk=?";
			pstmt = conn.prepareStatement(accSQL);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				accs.add(getAccountFromRS(rs));
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				} if (stmt != null) {
					stmt.close();
				} if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				logger.error("So this happened " + ex.getMessage() + " :(");
			}
		}
		
		return accs;
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
	
	private Client fetchClient(int id) throws DatabaseException, SQLException {
		Client client = null;
		if (conn == null) {
			throw new DatabaseException("Failure To Acquire Connection");
		}
		pstmt = conn.prepareStatement("SELECT * FROM clients.client WHERE id=?");
		pstmt.setInt(1, id);
		rs = pstmt.executeQuery();
		while (rs.next()) {				
			client = ClientRepo.getClientFromRS(rs);
		}
		return client;
	}
}
