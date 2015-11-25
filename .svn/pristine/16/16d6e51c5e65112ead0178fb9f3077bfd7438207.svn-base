package com.syntun.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileToHadoopLog {

	private static FileWriter logWriter;
	/**
	 * 分文件日期格式化对象
	 */
	private final static SimpleDateFormat DF = new SimpleDateFormat("yyyyMMddHH");
	
	
	private final static SimpleDateFormat logDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static String logDir;
	/**
	 * 当前后缀
	 */
	private static String nowFileSuffix;
	/**
	 * 设置日志写入文件,并制定其目录,如果不设置则只写入文件
	 * 
	 * @param logDir
	 * @throws IOException 
	 */
	synchronized static void setLogFile(String logFileDir) throws IOException {
		File f = new File(logFileDir);
		if(!f.exists()) {
			f.mkdir();
		}
		
		nowFileSuffix = DF.format(new Date());
		String fileName = logFileDir+"/log_"+nowFileSuffix+".txt";
		logDir = logFileDir;
		
		logWriter = new FileWriter(fileName);
		
		
	}
	
	public synchronized static void addMessage(String message) {
		System.out.println(message);
		try {
			addMessageToFile(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void addMessageToFile(String message) throws IOException {
		if(logWriter!=null) {
			Date nowDate = new Date();
			String nowSuffix = DF.format(nowDate);
			if(!nowSuffix.equals(nowFileSuffix)){
				setLogFile(logDir);
			}
			logWriter.write("["+logDF.format(nowDate)+"] "+message+"\r\n");
			logWriter.flush();
		}
	}
	
}
