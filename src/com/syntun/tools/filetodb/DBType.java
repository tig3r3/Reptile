package com.syntun.tools.filetodb;

public enum DBType {
	
	MySql(1,"MySql");
	
	 int dbTypeValue;
	
	private String dbTypeName;
	
	private DBType(int dbTypeValue, String dbTypeName) {
		this.dbTypeValue = dbTypeValue;
		this.dbTypeName = dbTypeName;
	}
	
	public String toString() {
		return dbTypeName;
	}
}
