package com.berry.dao;

import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;

import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berry.app.Application;
import com.berry.exception.ClientCreationException;
import com.berry.exception.DatabaseException;
import com.berry.model.Client;

public class ClientRepo {
	private transient String user;
	private transient String pass;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Statement stmt = null;
	private static Logger logger = LoggerFactory.getLogger(Application.class);

	public ClientRepo() {
		super();
	}

	public Client createClient(String fname, String lname) throws ClientCreationException, DatabaseException {
		Client client = null;
		try {
			connectToDB();
			pstmt = conn.prepareStatement("INSERT INTO clients.client(fname, lname) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, fname);
			pstmt.setString(2, lname);
			int count = pstmt.executeUpdate();
			if (count == 0) {
				throw new DatabaseException("No Records Were Updated.");
			}
			rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				int createdId = rs.getInt("id");
				ResultSet rs2;
				PreparedStatement pstmt2 = conn.prepareStatement("SELECT * FROM clients.client WHERE id=?");
				pstmt2.setInt(1, createdId);
				rs2 = pstmt2.executeQuery();
				while(rs2.next()) {
					client = getClientFromRS(rs2);
				}				
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

	public Client getClientById(int id) {
		Client client = null;

		// try to connect
		connectToDB();

		try {
			pstmt = conn.prepareStatement("SELECT * FROM clients.client WHERE id=?");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {				
				client = getClientFromRS(rs);
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

		return client;
	}

	public ArrayList<Client> getAllClients() {
		ArrayList<Client> clients = new ArrayList<Client>();

		// try to connect
		connectToDB();

		try {
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

	private Client getClientFromRS(ResultSet rs) throws SQLException {
		Client client = null;
		String fname = rs.getString("fname");
		String lname = rs.getString("lname");
		String joined = rs.getString("joined");
		int id = rs.getInt("id");
		client = new Client(fname, lname, joined, id);

		return client;
	}

	private void connectToDB() {
		try {
			Wini ini = new Wini(new File(Application.fileIniPath));
			final String URL = ini.get("database", "servername");
			user = ini.get("database", "username");
			pass = ini.get("database", "password");
			conn = DriverManager.getConnection(URL, user, pass);
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
}
