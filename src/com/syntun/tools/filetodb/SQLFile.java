package com.syntun.tools.filetodb;

import java.io.File;

public class SQLFile {

	private String fileName;
	
	private String tableName;
	
	private String dbName;
	
	private long fileSuffix;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		File f = new File(fileName);
		fileName = f.getAbsolutePath();
		String[] fileNameArr = fileName.split("[\\\\/]");
		int i = 0;
		StringBuffer nFileName = new StringBuffer();
		for(String pathName : fileNameArr) {
			if(i!=0) nFileName.append('/');
			if(pathName.indexOf(" ")!=-1 && pathName.indexOf("\"")!=0) nFileName.append("\""+pathName+"\"");
			else nFileName.append(pathName);
			i++;
		}
		this.fileName = nFileName.toString();
	}

	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
		if(tableName.indexOf(".")!=-1) {
			String[] arr = tableName.split("\\.");
			this.dbName = arr[0];
		}
	}
	/**
	 * 
	 * @param fileName
	 * @param tableName
	 */
	public SQLFile(String fileName, String tableName, long fileSuffix) {
		this.setFileName(fileName);
		this.setTableName(tableName);
		this.setFileSuffix(fileSuffix);
	}

	
	@Override
	public boolean equals(Object a) {
		if(a.getClass().isInstance(this)) {
			if(((SQLFile)a).fileName.equals(this.fileName)) return true;
			else return false;
		}
		else return false;
	}

	public String getDbName() {
		return dbName;
	}

	public long getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(long fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
}
