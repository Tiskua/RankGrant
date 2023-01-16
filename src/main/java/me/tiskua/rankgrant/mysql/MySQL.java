package me.tiskua.rankgrant.mysql;

import me.tiskua.rankgrant.main.*;

import java.sql.*;

public class MySQL {
	
	private final String host = Files.config.getString("Database.host");
	private final String port = Files.config.getString("Database.port");
	private final String database = Files.config.getString("Database.database");
	private final String username = Files.config.getString("Database.username");
	private final String password = Files.config.getString("Database.password");
	
	private Connection connection;
	
	public boolean isConnected() {
		return (connection != null);
	}
	
	public void connect() throws SQLException{
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
