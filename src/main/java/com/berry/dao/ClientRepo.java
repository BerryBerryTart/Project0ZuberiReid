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
import com.berry.model.Client;

public class ClientRepo {
	private static String fileIniPath = "src/resources/db.ini";
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
	
	public void createClient() {
		
	}
	
	public Client getClientById(int id) {
		Client client = null;
		if (id == 1) {
			client = new Client("Jim", "John", "May");
		}		
		return client;
	}
	
	public ArrayList<Client> getAllClients(){
		ArrayList<Client> clients = new ArrayList<Client>();
		clients.add(new Client("BIll", "BOB", "April"));
		return clients;
	}
	
	private void connectToDB() {
		try {
			Wini ini = new Wini(new File(fileIniPath));
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
