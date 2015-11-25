package com.syntun.tools.filetodb;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * 利用mysqldump向mysql导入数据
 */
public class FileToMysql implements FileToDb {

	private MysqlDbInfo dbInfo;

	private final static String CMD_COMMAND_STR = "-h%s -P%s -u%s -p%s --default-character-set=GBK -f %s<%s";

	public FileToMysql(DBInfo dbInfo) {
		this.dbInfo = (MysqlDbInfo) dbInfo;
	}

	// 基本路径
	private static final String basePath = "/root/wgdata/";

	// 记录Shell执行状况的日志文件的位置(绝对路径)
	private static final String executeShellLogFile = basePath
			+ "executeShell.log";

	public boolean fileToDb(SQLFile sqlFile) {
		Formatter fmt = new Formatter();
		// String comStr =
		// "cmd /c "+dbInfo.getMysqlBinDir()+"mysql.exe "+fmt.format(CMD_COMMAND_STR,
		// dbInfo.getDbHost(), "3306", dbInfo.getDbUserName(),
		// dbInfo.getDbPassWord(), sqlFile.getDbName(),
		// sqlFile.getFileName()).toString();
		String shellCommand = dbInfo.getMysqlBinDir()
				+ "mysql "
				+ fmt.format(CMD_COMMAND_STR, dbInfo.getDbHost(), "3306",
						dbInfo.getDbUserName(), dbInfo.getDbPassWord(),
						sqlFile.getDbName(), sqlFile.getFileName()).toString();
		System.out.println("shellCommand:" + shellCommand);
		boolean success = false;
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader = null;
		// 格式化日期时间，记录日志时使用
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS ");

		try {
			stringBuffer.append(dateFormat.format(new Date()))
					.append("准备执行Shell命令 ").append(shellCommand)
					.append(" \r\n");
			Process pid = null;
			String[] cmd = { "/bin/sh", "-c", shellCommand };
			// 执行Shell命令
			pid = Runtime.getRuntime().exec(cmd);
			if (pid != null) {
				stringBuffer.append("进程号：").append(pid.toString())
						.append("\r\n");
				// bufferedReader用于读取Shell的输出内容
				bufferedReader = new BufferedReader(new InputStreamReader(
						pid.getInputStream()), 1024);
				pid.waitFor();
			} else {
				stringBuffer.append("没有pid\r\n");
			}
			pid.destroy();
			stringBuffer.append(dateFormat.format(new Date())).append(
					"Shell命令执行完毕\r\n执行结果为：\r\n");
			String line = null;
			// 读取Shell的输出内容，并添加到stringBuffer中
			while (bufferedReader != null
					&& (line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line).append("\r\n");
			}
			System.out.println("stringBuffer:" + stringBuffer);
		} catch (Exception ioe) {
			stringBuffer.append("执行Shell命令时发生异常：\r\n").append(ioe.getMessage())
					.append("\r\n");
		} finally {
			if (bufferedReader != null) {
				OutputStreamWriter outputStreamWriter = null;
				try {
					bufferedReader.close();
					// 将Shell的执行情况输出到日志文件中
					OutputStream outputStream = new FileOutputStream(
							executeShellLogFile);
					outputStreamWriter = new OutputStreamWriter(outputStream,
							"UTF-8");
					outputStreamWriter.write(stringBuffer.toString());
					System.out.println("stringBuffer.toString():"
							+ stringBuffer.toString());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						outputStreamWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			success = true;
		}
		return success;
	}

	// @SuppressWarnings("resource")
	// @Override
	// public boolean fileToDb(SQLFile sqlFile) {
	// // TODO Auto-generated method stub
	// Formatter fmt = new Formatter();
	// // String comStr =
	// "cmd /c "+dbInfo.getMysqlBinDir()+"mysql.exe "+fmt.format(CMD_COMMAND_STR,
	// dbInfo.getDbHost(), "3306", dbInfo.getDbUserName(),
	// dbInfo.getDbPassWord(), sqlFile.getDbName(),
	// sqlFile.getFileName()).toString();
	// String comStr =
	// dbInfo.getMysqlBinDir()+"mysql "+fmt.format(CMD_COMMAND_STR,
	// dbInfo.getDbHost(), "3306", dbInfo.getDbUserName(),
	// dbInfo.getDbPassWord(), sqlFile.getDbName(),
	// sqlFile.getFileName()).toString();
	// String[] cmd = { "/bin/sh", "-c", comStr };
	// System.out.println("+++++++++++++++++++++++++++++");
	// System.out.println(comStr);
	// System.out.println("+++++++++++++++++++++++++++++");
	// try {
	// Process p = Runtime.getRuntime().exec(cmd);
	// InputStream in = p.getErrorStream();
	// BufferedReader br = new BufferedReader(new InputStreamReader(in,
	// "gb2312"));
	// String str = br.readLine();
	// for(;(str = br.readLine()) != null;){
	// FileToDbLog.addMessage(str);
	// }
	// br.close();
	// in.close();
	// p.destroy();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return false;
	// }

}
