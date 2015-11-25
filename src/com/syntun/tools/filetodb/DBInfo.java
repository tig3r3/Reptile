package com.syntun.tools.filetodb;
/**
 * 数据库信息bean
 */
public class DBInfo {
	
	private String dbHost;
	
	private String dbUserName;
	
	private String dbPassWord;
	
	private DBType dbType;
	/**
	 * 
	 * @param dbHost	主机名
	 * @param dbUserName	数据库用户名
	 * @param dbPassWord	
	 */
	public DBInfo(String dbHost, String dbUserName, String dbPassWord, DBType dbType) {
		this.dbHost = dbHost;
		this.dbUserName = dbUserName;
		this.dbPassWord = dbPassWord;
		this.dbType = dbType;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbPassWord() {
		return dbPassWord;
	}

	public void setDbPassWord(String dbPassWord) {
		this.dbPassWord = dbPassWord;
	}

	public DBType getDbType() {
		return dbType;
	}

	public void setDbType(DBType dbType) {
		this.dbType = dbType;
	}
}
