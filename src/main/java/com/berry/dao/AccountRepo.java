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
import com.berry.dto.AccQueryDTO;
import com.berry.dto.AccountDTO;
import com.berry.exception.BadParameterException;
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
	
	public ArrayList<Account> getAllAccounts(int id, AccQueryDTO accQueryDTO) throws DatabaseException, NotFoundException, BadParameterException {
		ArrayList<Account> accs = new ArrayList<Account>();
		
		try {
			conn = ConnectionUtil.connectToDB();
			fetchClient(id);
			String accSQL = "SELECT * FROM clients.account WHERE fk=?";
			
			//Optional Queries here
			if (accQueryDTO.lessThanNotEmpty()) {
				int lessThan = Integer.parseInt(accQueryDTO.getLessThan());
				accSQL = accSQL.concat(" AND balance < " + lessThan + " ");
			}
			if (accQueryDTO.greaterThanNotEmpty()) {
				int greaterThan = Integer.parseInt(accQueryDTO.getGreaterThan());
				accSQL = accSQL.concat(" AND balance > " + greaterThan + " ");
			}
			
			pstmt = conn.prepareStatement(accSQL);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				accs.add(getAccountFromRS(rs));
			}
			
		} catch (NumberFormatException e) {
			throw new BadParameterException("Query Param must be an integer.");
		}
		catch (SQLException e) {
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
	
	public Account getAccountFromID(int id, int accId) throws DatabaseException, NotFoundException {
		Account acc = null;
		try {
			conn = ConnectionUtil.connectToDB();
			fetchClient(id);
			String accSQL = "SELECT * FROM clients.account WHERE id=? AND fk=?";
			pstmt = conn.prepareStatement(accSQL);
			pstmt.setInt(1, accId);
			pstmt.setInt(2, id);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				acc = getAccountFromRS(rs);
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
		return acc;
	}	

	public Account createAccount(int id, AccountDTO accDTO) throws DatabaseException, NotFoundException {
		Account acc = null;
		try {
			conn = ConnectionUtil.connectToDB();
			fetchClient(id);
			String sql = "INSERT INTO clients.account (`type`, acc_num, balance, fk)"
					+ " VALUES (?,?,?,?)";
			
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, accDTO.getType());
			pstmt.setInt(2, accDTO.getAcc_num());
			pstmt.setDouble(3, accDTO.getBalance());
			pstmt.setInt(4, id);
			
			int count = pstmt.executeUpdate();
			if (count == 0) {
				throw new DatabaseException("No Records Were Updated.");
			}
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				acc = new Account(accDTO.getType(), accDTO.getAcc_num(), accDTO.getBalance());
				acc.setId(rs.getInt("id"));
			}			
		} catch (SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
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
		return acc;
	}
	
	public Account updateAccount(int id, int accId, AccountDTO accDTO) throws DatabaseException, NotFoundException {
		Account acc = null;
		try {
			conn = ConnectionUtil.connectToDB();
			
			fetchClient(id);
			
			//IT IS IMPORTANT to update by pk and fk
			String sql = "UPDATE clients.account SET type=?, balance=?"
					+ " WHERE id=? AND fk=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, accDTO.getType());
			pstmt.setDouble(2, accDTO.getBalance());
			pstmt.setInt(3, accId);
			pstmt.setInt(4, id);
			
			int count = pstmt.executeUpdate();
			if (count == 0) {
				throw new DatabaseException("No Records Were Updated.");
			}
			
			acc = new Account(accDTO.getType(), accDTO.getAcc_num(), accDTO.getBalance());
			acc.setId(accId);
						
		} catch (SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
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
		return acc;
	}
	
	public boolean deleteAccount(int id, int accId) throws DatabaseException, NotFoundException {
		boolean success = false;
		try {
			conn = ConnectionUtil.connectToDB();
			fetchClient(id);
			//IT IS IMPORTANT to delete by pk and fk
			String sql = "DELETE FROM clients.account WHERE id=? AND fk=?";
			pstmt = conn.prepareStatement(sql);	
			pstmt.setInt(1, accId);
			pstmt.setInt(2, id);
			
			int count = pstmt.executeUpdate();
			if (count == 0) {
				throw new DatabaseException("No Records Were Updated.");
			}
			success = true;
		} catch (SQLException ex) {
			logger.error("SQLException: " + ex.getMessage());
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
		return success;
	}
	
	public static Account getAccountFromRS(ResultSet rs) throws SQLException{
		Account acc = null;
		int id = rs.getInt("id");
		String type = rs.getString("type");
		String created = rs.getString("created");
		int acc_num = rs.getInt("acc_num");
		double balance = rs.getDouble("balance");
		
		acc = new Account(type, acc_num, balance);
		acc.setId(id);
		acc.setCreated(created);
		return acc;
	}
	
	private Client fetchClient(int id) throws DatabaseException, SQLException, NotFoundException {
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
		if (client == null) {
			throw new NotFoundException("No Client Found");
		}
		return client;
	}
}
