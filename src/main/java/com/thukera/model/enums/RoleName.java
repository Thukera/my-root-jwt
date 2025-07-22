package com.thukera.model.enums;

public enum RoleName {
	ROLE_USER("user"), ROLE_PM("pm"), ROLE_ADMIN("admin"), ROLE_PARAMS("params"), ROLE_ZABBIX("zabbix");

	private String key;

	RoleName(String key) {
		this.key = key;
	}

	public static RoleName getRole(String key) {
		for (RoleName n : values()) {
			if (n.key.equalsIgnoreCase(key)) {
				return n;
			}
		}
		return null;

	}

}