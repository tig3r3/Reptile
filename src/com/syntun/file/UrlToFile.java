package com.syntun.file;

import java.io.BufferedReader;
import java.io.File;
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
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.syntun.webget.UrlSupervise;

public class UrlToFile {

	/**
	 * sql语句缓存文件目录
	 */
	private static String sqlCacheFilePath = "./content/";
	/**
	 * 记录导入状态的文件路径
	 */
	private static String insertStatusFileUrl = sqlCacheFilePath
			+ "insert_status_log.txt";
	/**
	 * 记录未导入可导入的表名称
	 */
	private static Set<ContentFile> insertFileSet = new LinkedHashSet<ContentFile>();
	/**
	 * 正在写入的表
	 */
	private static Set<ContentFile> writeFileSet = new LinkedHashSet<ContentFile>();
	/**
	 * 分文件日期格式化对象
	 */
	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");

	/**
	 * 缓存文件目录格式化
	 */
	private static SimpleDateFormat ddf = new SimpleDateFormat("yyyy-MM-dd-HH");

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
	private static Map<String, UrlToFile> tableFileObjMap = new HashMap<String, UrlToFile>();

	/**
	 * 初始化数据缓存
	 * 
	 * @param dbInfo
	 */
	static {
		inspectPathExists();
		isStart = true;
	}

	/**
	 * @param 平台加sort_id
	 * @return
	 * @throws Exception
	 */
	public synchronized static UrlToFile getFileToDataBase(String urlGroup)
			throws Exception {
		if (!isStart)
			throw new Exception("尚未初始化！");
//		if (urlGroup.indexOf("_45") != -1 || urlGroup.indexOf("550") != -1) {
//			int nums = (int) (Math.random() * 10);
//			urlGroup = urlGroup + "_" + nums;
//		}
		if (!tableFileObjMap.containsKey(urlGroup)) {
			tableFileObjMap.put(urlGroup, new UrlToFile(urlGroup));
		}
		return tableFileObjMap.get(urlGroup);
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
				System.out.println("创建数据缓存文件失败！...");
				System.exit(0);
			}
		}
		// 将未导入hadoop状态文件装入集合
		try {
			BufferedReader br = new BufferedReader(new FileReader(logF));
			String row;
			while ((row = br.readLine()) != null) {
				String[] ContentFileStatusArr = row.split(SPLIT_STR);
				if (ContentFileStatusArr[1].equals("true"))
					continue;
				else {
					ContentFile ContentFile = new ContentFile(
							ContentFileStatusArr[0], ContentFileStatusArr[2],
							Long.parseLong(ContentFileStatusArr[3]));
					insertFileSet.add(ContentFile);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取数据缓存文件失败！...");
			System.exit(0);
		}

		try {
			FileToHadoopLog.setLogFile(sqlCacheFilePath + "log/");
		} catch (IOException e) {
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
		Set<ContentFile> logContentFileSet = new LinkedHashSet<ContentFile>();
		synchronized (insertFileSet) {
			synchronized (writeFileSet) {
				logContentFileSet.addAll(insertFileSet);
				logContentFileSet.addAll(writeFileSet);
			}
		}
		File f = new File(insertStatusFileUrl);
		try {
			FileWriter logWriter = new FileWriter(f);
			StringBuffer writerLogStr = new StringBuffer();
			if (logContentFileSet.size() > 0)
				for (ContentFile ContentFile : logContentFileSet)
					writerLogStr.append(ContentFile.getFileName() + SPLIT_STR
							+ "false" + SPLIT_STR + ContentFile.getTableName()
							+ SPLIT_STR + ContentFile.getFileSuffix() + "\r\n");
			else
				writerLogStr.append("");
			logWriter.write(writerLogStr.toString());
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成页面内容文件
	 * 
	 * @throws IOException
	 */
	private synchronized static FileWriter getContentFileWrite(String urlGroup,
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
			ContentFile insertFile = null;
			for (ContentFile f : writeFileSet) {
				if (f.getTableName().equals(urlGroup)) {
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
					fileName = dirName + urlGroup + "_" + UUID.randomUUID()
							+ "" + UrlSupervise.localhostIp + "_" + i + ".txt";
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
			writeFileSet.add(new ContentFile(fileName, urlGroup, dateSuffix));
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
			while (true) {
				writeLogFile();
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 导入数据线程
	 */
	static class FileToData implements Runnable {

		FileToHadoop24 ftd;

		public FileToData() {
			ftd = new FileToHadoop24();
		}

		@Override
		public void run() {
			while (true) {
				Set<ContentFile> fileSet;
				Set<ContentFile> writerSet;
				Long suffix = getFileSuffix();

				synchronized (insertFileSet) {
					synchronized (writeFileSet) {
						writerSet = new LinkedHashSet<ContentFile>(writeFileSet);
						for (ContentFile ContentFile : writerSet) {
							if (ContentFile.getFileSuffix() != suffix) {
								writeFileSet.remove(ContentFile);
								insertFileSet.add(ContentFile);
							}
						}
					}
					fileSet = new LinkedHashSet<ContentFile>(
							(LinkedHashSet<ContentFile>) insertFileSet);
				}
				for (ContentFile fileObj : fileSet) {
					FileToHadoopLog.addMessage("开始向hadoop导入文件"
							+ fileObj.getFileName());
					ftd.fileToDb(fileObj);
					FileToHadoopLog.addMessage("完成向hadoop导入文件"
							+ fileObj.getFileName());
					synchronized (insertFileSet) {
						insertFileSet.remove(fileObj);
					}

				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * 平台和sort_id的组成
	 */
	private String urlGroup;

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

	private long maxFileSize = 200 * 1024 * 1024;
	private Pattern pNow = Pattern.compile("now\\(\\)",
			Pattern.CASE_INSENSITIVE);
	private SimpleDateFormat nowDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 构造函数
	 * 
	 * @param urlGroup
	 */
	private UrlToFile(String urlGroup) {
		this.urlGroup = urlGroup;
		this.fileNameSuffix = getFileSuffix();
		fw = getContentFileWrite(urlGroup, fileNameSuffix);
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
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			fw = getContentFileWrite(this.urlGroup, nowSuffix);
			fileSize = 0;
			this.fileNameSuffix = nowSuffix;
		}
		try {
			if (sql.indexOf("now()") != -1 || sql.indexOf("NOW()") != -1) {
				Matcher m = pNow.matcher(sql);
				sql = m.replaceAll("\"" + nowDateFormat.format(new Date())
						+ "\"");
			}
			String sqlStr = sql + ";\r\n";
			this.fileSize += sql.length();
			fw.write(sqlStr);
			fw.flush();
		} catch (IOException e) {
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
		UrlToFile.inspectPathExists();
		UrlToFile ftdb = new UrlToFile("test.aaa");
		for (int i = 0; i < 1000; i++) {
			ftdb.addSql("insert test.aaa (a,b) values(" + i + ",'中文')");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
