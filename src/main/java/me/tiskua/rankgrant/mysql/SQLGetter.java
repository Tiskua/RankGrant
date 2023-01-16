package me.tiskua.rankgrant.mysql;

import me.tiskua.rankgrant.Grant.*;
import me.tiskua.rankgrant.main.*;
import org.bukkit.entity.*;

import java.sql.*;
import java.util.*;

public class SQLGetter {

	Main plugin = Main.getMainInstance();

	public void createTable() {
		PreparedStatement ps;
		try {
			ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS grants "
					+ "(GRANTID INT(100) AUTO_INCREMENT,TARGET VARCHAR(100),UUID VARCHAR(100),GRANTS VARCHAR(100),DURATION INT(100),"
					+ "REASON VARCHAR(100),GRANTER VARCHAR(100),PRIMARY KEY (GRANTID))");
			ps.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public void createMYSQLLog(Player player) {
		if(!plugin.SQL.isConnected()) return;
		try {
			UUID target_uuid = player.getUniqueId();
			
			PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT INTO grants (TARGET,UUID,GRANTS,DURATION,REASON,GRANTER) VALUES (?,?,?,?,?,?)");
			ps2.setString(1, player.getName());
			ps2.setString(2, target_uuid.toString());
			ps2.setString(3, GrantManager.getGrantType() == GrantManager.GrantType.RANK ?
					GrantManager.getTargetRank() : GrantManager.getTargetPermission() + " | " + GrantManager.getPermissionBoolean());
			ps2.setInt(4, GrantManager.getGrantDuration());
			ps2.setString(5, GrantManager.getGrantReason());
			ps2.setString(6, GrantManager.getGranter().getName());
			ps2.executeUpdate();

		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
