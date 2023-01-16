package me.tiskua.rankgrant.Grant;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class GrantManager {
	public enum GrantType {
		RANK,
		PERMISSION
	}
	private static @Getter @Setter Player granter;
	private static @Getter @Setter String target;
	private static @Getter @Setter GrantType grantType;
	private static @Getter @Setter String targetRank;
	private static @Getter @Setter int grantDuration;
	private static @Getter @Setter String grantReason;
	private static @Getter @Setter String targetPermission;
	private static @Getter @Setter String permissionBoolean;
	
}
