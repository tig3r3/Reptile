package com.syntun.tools.filetodb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 将文件中的sql语句导入数据库,利用mysqldump,只适用于mysql
 * 
 * 待解决问题 1.仅支持mysql 2.只能针对一个数据库
 * 
 */
public class FileToDataBase {
	/**
	 * sql语句缓存文件目录
	 */
	private static String sqlCacheFilePath = "./cache_data/";
	/**
	 * 记录导入状态的文件路径
	 */
	private static String insertStatusFileUrl = sqlCacheFilePath
			+ "insert_status_log.txt";
	/**
	 * 记录未导入可导入的表名称
	 */
	private static Set<SQLFile> insertFileSet = new LinkedHashSet<SQLFile>();
	/**
	 * 正在写入的表
	 */
	private static Set<SQLFile> writeFileSet = new LinkedHashSet<SQLFile>();
	/**
	 * 分文件日期格式化对象
	 */
	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
	/**
	 * 缓存文件目录格式化
	 */
	private static SimpleDateFormat ddf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 数据库联接信息
	 */
	private static DBInfo dbInfo;
	/**
	 * 日志文件字段分割字符
	 */
	private static final String SPLIT_STR = "' '";
	/**
	 * 是否已启动
	 */
	private static boolean isStart = false;
	/**
	 * 已创建文件导入数据库对象
	 */
	private static Map<String, FileToDataBase> tableFileObjMap = new HashMap<String, FileToDataBase>();

	/**
	 * 初始化数据缓存
	 * 
	 * @param dbInfo
	 */
	public synchronized static void sqlCacheFileInit(DBInfo dbInfo) {
		FileToDataBase.dbInfo = dbInfo;
		inspectPathExists();
		isStart = true;
	}

	/**
	 * 
	 * @param tableName
	 *            完整数据库名 databaseName.tableName
	 * @return
	 * @throws Exception
	 */
	public synchronized static FileToDataBase getFileToDataBase(String tableName)
			throws Exception {
		if (!isStart)
			throw new Exception("尚未初始化！");
		if (!tableFileObjMap.containsKey(tableName)) {
			tableFileObjMap.put(tableName, new FileToDataBase(tableName));
		}
		return tableFileObjMap.get(tableName);
	}

	/**
	 * 检查数据缓存文件目录是否已存在，不存在则创建
	 */
	private static void inspectPathExists() {
		// 检查目录是否存在
		File f = new File(sqlCacheFilePath);
		if (!f.exists()) {
			f.mkdir();
		}
		// 检查状态文件是否存在
		File logF = new File(insertStatusFileUrl);
		if (!logF.exists()) {
			try {
				logF.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("创建数据缓存文件失败！...");
				System.exit(0);
			}
		}
		// 将未导入数据库状态文件装入集合
		try {
			BufferedReader br = new BufferedReader(new FileReader(logF));
			String row;
			while ((row = br.readLine()) != null) {
				String[] sqlFileStatusArr = row.split(SPLIT_STR);
				if (sqlFileStatusArr[1].equals("true"))
					continue;
				else {
					SQLFile sqlFile = new SQLFile(sqlFileStatusArr[0],sqlFileStatusArr[2],Long.parseLong(sqlFileStatusArr[3]));
					insertFileSet.add(sqlFile);
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("读取数据缓存文件失败！...");
			System.exit(0);
		}

		try {
			FileToDbLog.setLogFile(sqlCacheFilePath + "log/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("创建日志文件失败");
			System.exit(0);
		}
		new Thread(new FileToData()).start();
		new Thread(new logThread()).start();
	}

	/**
	 * 把未导入数据库的文件添加到日志中
	 */
	public static void writeLogFile() {
		Set<SQLFile> logSqlFileSet = new LinkedHashSet<SQLFile>();
		synchronized (insertFileSet) {
			synchronized (writeFileSet) {
				logSqlFileSet.addAll(insertFileSet);
				logSqlFileSet.addAll(writeFileSet);
			}

		}
		File f = new File(insertStatusFileUrl);
		try {
			FileWriter logWriter = new FileWriter(f);
			StringBuffer writerLogStr = new StringBuffer();
			if (logSqlFileSet.size() > 0)
				for (SQLFile sqlFile : logSqlFileSet)
					writerLogStr.append(sqlFile.getFileName() + SPLIT_STR+ "false" + SPLIT_STR + sqlFile.getTableName()+ SPLIT_STR + sqlFile.getFileSuffix() + "\r\n");
			else
				writerLogStr.append("");
			// System.err.println(writerLogStr.toString());
			logWriter.write(writerLogStr.toString());
			logWriter.close();
		} catch (FileNotFoundException e) {
			System.out.println("未生成待插入文件");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成sql文件
	 * 
	 * @throws IOException
	 */
	private synchronized static FileWriter getSqlFileWrite(String tableName,
			long dateSuffix) {

		String dirName = sqlCacheFilePath + ddf.format(new Date()) + "/";

		// 检查目录是否存在
		File dirF = new File(dirName);
		if (!dirF.exists()) {
			dirF.mkdir();
		}

		int i = 0;
		FileWriter fw = null;
		String fileName = null;

		synchronized (writeFileSet) {
			SQLFile insertFile = null;
			for (SQLFile f : writeFileSet) {
				if (f.getTableName().equals(tableName)) {
					insertFile = f;
					break;
				}
			}
			try {
				if (insertFile != null) {
					writeFileSet.remove(insertFile);
					insertFileSet.add(insertFile);
				}
				while (true) {
					fileName = dirName + tableName + "_" + dateSuffix + "_" + i
							+ ".sql";
					File f = new File(fileName);
					if (!f.exists()) {
						f.createNewFile();
						fw = new FileWriter(f);
						break;
					}
					i++;
				}
			} catch (IOException e) {
				System.out.println("创建文件失败!!!");
				System.exit(0);
			}
			writeFileSet.add(new SQLFile(fileName, tableName, dateSuffix));
		}

		return fw;
	}

	/**
	 * 获取文件后缀
	 * 
	 * @return
	 */
	private synchronized static long getFileSuffix() {
		return Long.parseLong(df.format(new Date()));
	}

	/**
	 * 写导入状态线程
	 */
	static class logThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				writeLogFile();
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 导入数据线程
	 */
	static class FileToData implements Runnable {

		FileToDb ftd;

		public FileToData() {
			switch (dbInfo.getDbType()) {
			case MySql:
				ftd = new FileToMysql(dbInfo);
				break;
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			while (true) {
				Set<SQLFile> fileSet;
				Set<SQLFile> writerSet;
				Long suffix = getFileSuffix();

				synchronized (insertFileSet) {
					synchronized (writeFileSet) {
						writerSet = new LinkedHashSet<SQLFile>(writeFileSet);
						for (SQLFile sqlFile : writerSet) {
							if (sqlFile.getFileSuffix() != suffix) {
								writeFileSet.remove(sqlFile);
								insertFileSet.add(sqlFile);
							}
						}
					}
					fileSet = new LinkedHashSet<SQLFile>(
							(LinkedHashSet<SQLFile>) insertFileSet);
				}
				for (SQLFile fileObj : fileSet) {
					FileToDbLog
							.addMessage("开始向数据库导入文件" + fileObj.getFileName());
					ftd.fileToDb(fileObj);
					FileToDbLog
							.addMessage("完成向数据库导入文件" + fileObj.getFileName());
					synchronized (insertFileSet) {
						insertFileSet.remove(fileObj);
					}

				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * 表名称
	 */
	private String tableName;

	/**
	 * 当前文件后缀
	 */
	private long fileNameSuffix;
	/**
	 * 
	 */
	private FileWriter fw;
	/**
	 * 文件大小
	 * 
	 */
	private long fileSize = 0;

	private long maxFileSize = 100 * 1000 * 1000;
	// 仅对mysql ...
	private Pattern pNow = Pattern.compile("now\\(\\)",
			Pattern.CASE_INSENSITIVE);
	private SimpleDateFormat nowDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 构造函数
	 * 
	 * @param tableName
	 */
	private FileToDataBase(String tableName) {
		this.tableName = tableName;
		this.fileNameSuffix = getFileSuffix();
		fw = getSqlFileWrite(tableName, fileNameSuffix);
	}

	/**
	 * 添加sql语句到文件
	 * 
	 * @param sql
	 */
	public void addSql(String sql) {
		long nowSuffix = getFileSuffix();
		if (nowSuffix != fileNameSuffix || fileSize > maxFileSize) {
			try {
				System.out.println();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fw = getSqlFileWrite(this.tableName, nowSuffix);
			fileSize = 0;
			this.fileNameSuffix = nowSuffix;
		}
		try {
			// 这个效率成问题,暂时这样处理 ... mysql only
			if (sql.indexOf("now()") != -1 || sql.indexOf("NOW()") != -1) {
				Matcher m = pNow.matcher(sql);
				sql = m.replaceAll("\"" + nowDateFormat.format(new Date())
						+ "\"");
			}
			String sqlStr = sql + ";\r\n";

			this.fileSize += sqlStr.length();
			fw.write(sqlStr);
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void addListSql(List<String> listSql) {
		for (String sql : listSql) {
			this.addSql(sql);
		}
	}

	public static void main(String[] args) {
		FileToDataBase.dbInfo = new MysqlDbInfo("127.0.0.1", "root", "123456");
		FileToDataBase.inspectPathExists();
		FileToDataBase ftdb = new FileToDataBase("test.aaa");
		for (int i = 0; i < 10000; i++) {
			ftdb.addSql("insert test.aaa (a,b) values(" + i + ",'中文')");
			// if(i==166){
			// FileToDataBase.writeLogFile();
			// System.exit(0);
			// }
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// FileToDataBase ftdb = new FileToDataBase("test");
		// ftdb.addSql("insert test.aaa (a) values(0)");
	}
}
