package com.berry.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berry.app.Application;
import com.berry.exception.DatabaseException;

public class ConnectionUtil {
	private static transient String user;
	private static transient String pass;
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static Connection connectToDB() throws DatabaseException {
		Connection conn = null;
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
			throw new DatabaseException("Failure To Connect To Database");			
		}
		return conn;
	}	
}
