package com.berry.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.berry.app.Application;

public class DatabaseInit {
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	private static transient String URL;
	private static transient String user;
	private static transient String pass;
	
	private static Connection conn;
	private static Statement stmt;
	
	private static void getCreds() {
		Wini ini;
		try {
			ini = new Wini(new File(Application.fileIniPath));
			URL = ini.get("database", "servername");
			user = ini.get("database", "username");
			pass = ini.get("database", "password");
		} catch (InvalidFileFormatException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
	}
	
	public static boolean initDB() {
		boolean success = false;
		try {
			DatabaseInit.getCreds();
			conn = DriverManager.getConnection(URL, user, pass);
			stmt = conn.createStatement();
			
			String tbInit = "CREATE DATABASE IF NOT EXISTS clients";
			String selDB = "USE clients";
			String dbClientInit = "CREATE TABLE IF NOT EXISTS `clients.client` ("
					+ "	`id` INT(10) NOT NULL AUTO_INCREMENT,\r\n"
					+ "	`fname` VARCHAR(255) NOT NULL COLLATE 'latin1_swedish_ci',"
					+ "	`lname` VARCHAR(255) NOT NULL COLLATE 'latin1_swedish_ci',"
					+ "	`joined` DATETIME NULL DEFAULT current_timestamp(),"
					+ "	PRIMARY KEY (`id`) USING BTREE,"
					+ "	INDEX `id` (`id`) USING BTREE"
					+ ")"
					+ "COLLATE='latin1_swedish_ci'"
					+ "ENGINE=InnoDB";
			stmt.addBatch(tbInit);
			stmt.addBatch(selDB);
			stmt.addBatch(dbClientInit);
			stmt.executeBatch();
			success = true;
		} catch (SQLException e) {
			logger.error("DB Init Error " + e.getMessage());
			success = false;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				} if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException ex) {
				logger.error("So this happened " + ex.getMessage() + " :(");
			}
		}
		return success;
	}
}
