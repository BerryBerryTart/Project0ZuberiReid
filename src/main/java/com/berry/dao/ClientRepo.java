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
import com.berry.dto.ClientDTO;
import com.berry.exception.CreationException;
import com.berry.exception.DatabaseException;
import com.berry.model.Account;
import com.berry.model.Client;

public class ClientRepo{
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Statement stmt = null;
	private static Logger logger = LoggerFactory.getLogger(Application.class);

	public ClientRepo() {
		super();
	}
	
	public ArrayList<Client> getAllClients() throws DatabaseException {
		ArrayList<Client> clients = new ArrayList<Client>();
		
		try {
			// try to connect
			conn = ConnectionUtil.connectToDB();
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM clients.client");
			while (rs.next()) {
				clients.add(getClientFromRS(rs));
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

		return clients;
	}
	
	public Client getClientById(int id) throws DatabaseException {
		Client client = null;		

		try {
			// try to connect
			conn = ConnectionUtil.connectToDB();
			
			pstmt = conn.prepareStatement("SELECT * FROM clients.client WHERE id=?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {				
				client = getClientFromRS(rs);
			}
			client.setAccounts(fetchAccounts(id));
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
		return client;
	}	

	public Client createClient(ClientDTO clientDTO) throws CreationException, DatabaseException {
		Client client = null;
		
		try {
			conn = ConnectionUtil.connectToDB();
			
			String sql = "INSERT INTO clients.client(fname, lname) VALUES (?,?)";
			
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, clientDTO.getFname());
			pstmt.setString(2, clientDTO.getLname());
			int count = pstmt.executeUpdate();
			
			if (count == 0) {
				throw new DatabaseException("No Records Were Updated.");
			}
			
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				int createdId = rs.getInt("id");
				client = new Client(clientDTO.getFname(), clientDTO.getLname(), createdId);
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
		return client;
	}	

	public Client updateClient(int id, ClientDTO clientDTO) throws DatabaseException {
		Client client = null;
		
		try {
			String sql = "UPDATE clients.client SET fname=?, lname=? WHERE id=?";
			conn = ConnectionUtil.connectToDB();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, clientDTO.getFname());
			pstmt.setString(2, clientDTO.getLname());
			pstmt.setInt(3, id);
			
			int count = pstmt.executeUpdate();
			if (count == 0) {
				throw new DatabaseException("No Records Were Updated.");
			}		
			
			client = new Client(clientDTO.getFname(), clientDTO.getLname(), id);
					
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
		return client;
	}
	
	public boolean deleteClientById(int id) throws DatabaseException {
		boolean success = false;
		
		try {
			conn = ConnectionUtil.connectToDB();
			
			String sql = "DELETE FROM clients.client WHERE id=?";
			pstmt = conn.prepareStatement(sql);	
			pstmt.setInt(1, id);
			
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
	
	public static Client getClientFromRS(ResultSet rs) throws SQLException {
		Client client = null;
		String fname = rs.getString("fname");
		String lname = rs.getString("lname");
		String joined = rs.getString("joined");
		int id = rs.getInt("id");
		
		client = new Client(fname, lname, id);
		client.setJoined(joined);
		
		return client;
	}
	
	private ArrayList<Account> fetchAccounts(int id) throws DatabaseException, SQLException {
		ArrayList<Account> accs = new ArrayList<Account>();
		if (conn == null) {
			throw new DatabaseException("Failure To Acquire Connection");
		}
		pstmt = conn.prepareStatement("SELECT * FROM clients.account WHERE fk=?");
		pstmt.setInt(1, id);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			accs.add(AccountRepo.getAccountFromRS(rs));
		}
		return accs;
	}
}
