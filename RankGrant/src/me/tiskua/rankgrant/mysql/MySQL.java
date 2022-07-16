package me.tiskua.rankgrant.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.tiskua.rankgrant.main.Files;

public class MySQL {
	
	private String host = Files.config.getString("Database.host");
	private String port = Files.config.getString("Database.port");
	private String database = Files.config.getString("Database.database");
	private String username = Files.config.getString("Database.username");
	private String password = Files.config.getString("Database.password");
	
	private Connection connection;
	
	public boolean isConnected() {
		return (connection == null ? false : true);
	}
	
	public void connect() throws ClassNotFoundException, SQLException{
		if(!isConnected()) {

			connection = DriverManager.getConnection("jdbc:mysql://" +
				     host + ":" + port + "/" + database + "?useSSL=false",
				     username, password);
		
		}
	}
	public void disconnect() {
		if(isConnected()) {
			try {
				connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
}
