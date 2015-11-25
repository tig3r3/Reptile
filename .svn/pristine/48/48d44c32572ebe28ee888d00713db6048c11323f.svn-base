package com.syntun.tools.filetodb;

import java.io.File;

import com.syntun.webget.UrlSupervise;


public class MysqlDbInfo extends DBInfo {

	/**
	 * 
	 * 初始化
	 * 
	 * @param dbHost
	 * @param dbUserName
	 * @param dbPassWord
	 * @param mysqlBinDir
	 */
	public MysqlDbInfo(String dbHost, String dbUserName, String dbPassWord) {
		super(dbHost, dbUserName, dbPassWord, DBType.MySql);
		this.setMysqlDumpDir(UrlSupervise.mysqlBinDir);
	}

	public String getMysqlBinDir() {
		System.out.println("mysql path:"  + "---------------------");
		System.out.println();
		return UrlSupervise.mysqlBinDir;
	}

	public void setMysqlDumpDir(String mysqlDumpDir) {
		String[] dirArr = mysqlDumpDir.split("[\\/]");
		String nDir = "";
		int i = 0;
		for (String dirName : dirArr) {
			if (i != 0)
				nDir += "/";
			if (dirName.indexOf(" ") != -1) {
				if (i == 0) {
					if (dirName.indexOf(":") != -1) {
						nDir += dirName.replace(":", ":\"") + "\"";
					} else
						nDir += "\"" + dirName + "\"";
				} else
					nDir += "\"" + dirName + "\"";
			} else
				nDir += dirName;
			i++;
		}
		if (!nDir.substring(nDir.length() - 1, nDir.length()).equals("/"))
			nDir += "/";
		File file = new File(nDir.replace("\"", ""));
		if (!file.exists()) {
			System.out.println("未找到" + nDir);
			System.exit(0);
		}
		UrlSupervise.mysqlBinDir = nDir;
	}

}
