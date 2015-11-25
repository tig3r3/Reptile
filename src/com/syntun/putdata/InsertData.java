package com.syntun.putdata;

import java.lang.reflect.Method;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringEscapeUtils;
import com.syntun.tools.Base64;
import com.syntun.tools.ConnectSql;
import com.syntun.tools.SetDefaultProperty;
import com.syntun.tools.filetodb.FileToDataBase;
import com.syntun.tools.filetodb.MysqlDbInfo;
import com.syntun.webget.Url;
import com.syntun.wetget.annotation.SetProperty;

/**
 * 数据入库基类
 * 
 */
@SetProperty(propertyVars = { "maxChacheSqlLength", "chacheSqlLength",
		"maxInsertTime", "isTmpCacheData" }, propertyComment = "数据缓存设置")
public class InsertData {
	/**
	 * 临时数据库名称
	 */
	final static String TMP_DATABASE_NAME = "data_tmp";
	/**
	 * 表对象集合
	 */
	private static HashMap<Integer, InsertData> tableObjList = new HashMap<Integer, InsertData>();
	/**
	 * 表名称
	 */
	protected String tableName;
	/**
	 * 如果产生临时表后，该字段记录原表名称
	 */
	protected String srcTableName = null;
	/**
	 * 表信息
	 */
	protected HashMap<String, HashMap<String, String>> tableInfo;
	/**
	 * 检查信息对象
	 */
	protected InspectInformation ci;
	/**
	 * 表信息缓存
	 */
	private static HashMap<Integer, HashMap<String, HashMap<String, String>>> tableFiledInfo = new HashMap<Integer, HashMap<String, HashMap<String, String>>>();
	/**
	 * 储存的sql语句
	 */
	private LinkedList<String> sqlList = new LinkedList<String>();
	/**
	 * 最大缓存sql数量
	 */
	private static int maxChacheSqlLength = 400;
	/**
	 * 缓存sql数量
	 */
	private static int chacheSqlLength = 200;
	/**
	 * 最后一次插入时间
	 */
	private long lastaddSqlTime = new Date().getTime();
	/**
	 * 插入最大间隔时间
	 */
	private static long maxInsertTime = 2 * 60 * 1000;
	/**
	 * 表是否已经开始有数据插入
	 */
	protected boolean isRun = false;
	/**
	 * insert sql语句前部分
	 */
	private String insertSqlBefor = null;
	/**
	 * 字段，例：“filed1,filed2...”
	 */
	private String filedsStr = "";
	/**
	 * 是否建立缓存表
	 */
	private static boolean isTmpCacheData = true;
	/**
	 * 是否建立临时表的最小行数
	 */
	// private static int createrTmpTableMinRomNum = 1000000;
	/**
	 * 查找字符中是否包含unix编码正则对象
	 */
	private Pattern replaceUnicodePattern = Pattern
			.compile("[\\u0000-\\uffff]*?");
	/**
	 * 
	 */
	private Date createrTableObjDate = new Date();
	/**
	 * sql文件缓存对象
	 */
	private FileToDataBase sqlFileCache;
	private static boolean status = false;

	/**
	 * 获取表对象
	 * 
	 * @param tableId
	 * @return
	 */
	public synchronized static InsertData getTableObj(int tableId) {
		if (!status) {
			try {
				MysqlDbInfo mysqlDbInfo = new MysqlDbInfo(
						SetDefaultProperty
								.getProperty("com.syntun.tools.ConnectSql.sqlHost"),
						SetDefaultProperty
								.getProperty("com.syntun.tools.ConnectSql.sqlName"),
						SetDefaultProperty
								.getProperty("com.syntun.tools.ConnectSql.sqlPassWord"));
				FileToDataBase.sqlCacheFileInit(mysqlDbInfo);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("数据缓存初始化失败");
			}
			status = true;
		}
		InsertData tableObj;
		if (!tableObjList.containsKey(tableId)) {
			String tableName = InsertData.getTableName(tableId);
			if (tableName == null) {
				System.out.println("table name is null id=" + tableId);
				System.exit(0);
			}
			try {
				tableObj = (InsertData) Class.forName(tableName).newInstance();
			} catch (Exception e) {
				tableObj = new InsertData();
			}
			tableObj.tableName = tableName;
			tableObj.tableInfo = InsertData.getTableInfo(tableId, tableName);
			try {
				tableObj.sqlFileCache = FileToDataBase
						.getFileToDataBase(tableName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
			// 这里必须先为对象属性赋值tableObj.ci = new
			// InspectInformation(tableName);，然后才能判断是否要生成临时表isCreterCacheTable(tableObj);
			tableObj.ci = new InspectInformation(tableName);
			// isCreterCacheTable(tableObj);
			tableObjList.put(tableId, tableObj);
		} else
			tableObj = tableObjList.get(tableId);
		if (tableObj == null) {
			System.out.println(tableId);
			System.exit(0);
		}
		return tableObj;
	}

	/**
	 * 判断是否建立缓存表
	 */
	// private synchronized static void isCreterCacheTable(InsertData tableObj)
	// {
	// Connection conn = ConnectSql.getConn();
	// try {
	// Statement stmt = conn.createStatement();
	// String[] tableNameArr = tableObj.tableName.split("\\.");
	// String sql =
	// "SHOW TABLE STATUS FROM "+tableNameArr[0]+" WHERE NAME='"+tableNameArr[1]+"'";
	// ResultSet getCount = stmt.executeQuery(sql);
	// if(getCount.next()) {
	// if(getCount.getInt("Rows")>InsertData.createrTmpTableMinRomNum &&
	// isTmpCacheData) {
	// getCount.close();
	// String createrDataBaseSql =
	// "CREATE DATABASE IF NOT EXISTS "+TMP_DATABASE_NAME;
	// stmt.executeUpdate(createrDataBaseSql);
	// String tmpTableName =
	// TMP_DATABASE_NAME+"."+tableObj.tableName.replace(".", "_")+"_"+new
	// SimpleDateFormat("yyyyMMdd").format(new Date());
	// String createrTmpTalbeSql =
	// "CREATE TABLE IF NOT EXISTS "+tmpTableName+" like "+tableObj.tableName;
	// stmt.executeUpdate(createrTmpTalbeSql);
	// tableObj.srcTableName = tableObj.tableName;
	// tableObj.tableName = tmpTableName;
	// }
	// }
	// stmt.close();
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// ConnectSql.push(conn);
	//
	// }
	public static void resetInsertData() {
		InsertData.resetInsertData(true);
	}

	/**
	 * 重置
	 */
	public static void resetInsertData(boolean isToData) {
		clearInsertData(isToData);
		tableObjList.clear();
	}

	/**
	 * 清空缓存的数据,并停止线程
	 */
	public static void clearInsertData(boolean isToData) {
		FileToDataBase.writeLogFile();
		for (int i = 0; i < tableObjList.size(); i++) {
			InsertData tabObj = tableObjList.get(i);
			if (tabObj != null) {
				tabObj.isRun = false;
				synchronized (tabObj.sqlList) {
					tabObj.execSqlBatch(tabObj.sqlList);
				}
				if (isToData)
					tabObj.tmpToData();
			}
		}
	}

	/**
	 * 获取表信息
	 * 
	 * @param tableId
	 * @return
	 */
	private static String getTableName(int tableId) {
		// 查询表名称
		String sql = "select table_name from data_table_list where table_id="
				+ tableId;
		Connection conn = ConnectSql.getConn();
		String tableName = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
				tableName = rs.getString("table_name");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
		return tableName;
	}

	/**
	 * 获取表信息
	 * 
	 * @param tableIda
	 * @return
	 * @throws SQLException
	 */
	private static HashMap<String, HashMap<String, String>> getTableInfo(
			int tableId, String tableName) {

		if (!InsertData.tableFiledInfo.containsKey(tableId)) {
			Connection conn = ConnectSql.getConn();
			HashMap<String, HashMap<String, String>> hm = new HashMap<String, HashMap<String, String>>();
			try {
				Statement stmt = conn.createStatement();
				// 查询出表的所有字段信息
				String sql = "show full fields from data_table_filed_list";
				ResultSet rs = stmt.executeQuery(sql);
				LinkedList<String> filedList = new LinkedList<String>();
				while (rs.next())
					filedList.add(rs.getString("Field"));
				sql = "select * from data_table_list dtl join data_table_filed_list dtfl on(dtl.table_id=dtfl.table_id) where dtl.table_id="
						+ tableId;
				rs = stmt.executeQuery(sql);

				while (rs.next()) {
					HashMap<String, String> filedInfo = new HashMap<String, String>();
					for (int i = 0; i < filedList.size(); i++)
						filedInfo.put(filedList.get(i),
								rs.getString(filedList.get(i)));

					hm.put(rs.getString("filed_name"), filedInfo);
				}
				// 查询总行数判断是否需要建立临时表
				stmt.close();
			} catch (SQLException e) {
				hm = null;
				System.out.println("加载表信息失败!");
				System.exit(0);
			}
			tableFiledInfo.put(tableId, hm);
			ConnectSql.push(conn);
			return hm;
		} else
			return tableFiledInfo.get(tableId);
	}

	/**
	 * 添加sql语
	 * 
	 * @param sql
	 */
	public synchronized void addSql(List<String> insertSqlList) {
		if (isTmpCacheData) {
			this.sqlFileCache.addListSql(insertSqlList);
		} else {
			int sqlSize = sqlList.size();
			while (sqlSize >= maxChacheSqlLength) {
				try {
					Thread.sleep(200);
					sqlSize = sqlList.size();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			synchronized (sqlList) {
				if (insertSqlList != null)
					sqlList.addAll(insertSqlList);
				lastaddSqlTime = new Date().getTime();
			}
		}
	}

	/**
	 * 执行缓存的sql
	 */
	public void execSqlBatch(List<String> cacheSqlList) {

		// 同步代码块中检测缓存sql的数量
		int sqlSize = cacheSqlList.size();
		if (cacheSqlList != null && sqlSize != 0) {
			Connection conn = ConnectSql.getConn();
			try {
				conn.setAutoCommit(false);
				Statement stmt = conn.createStatement();
				for (int i = 0; i < sqlSize; i++) {
					stmt.addBatch(cacheSqlList.get(i));
				}
				Long bTime = System.currentTimeMillis();
				stmt.executeBatch();
				conn.commit();
				Long eTime = System.currentTimeMillis();
				System.out.println(this.tableName
						+ " insert sql exec time................."
						+ (eTime - bTime) + "_____" + sqlSize);
				stmt.close();
			} catch (BatchUpdateException bue) {
				int[] exeStatusArr = bue.getUpdateCounts();
				bue.printStackTrace();
				System.out
						.println("-------------\n\nerror sql start-------------------------");
				bue.printStackTrace();
				for (int i = 0; i < sqlSize; i++) {
					if (exeStatusArr[i] == Statement.EXECUTE_FAILED) {
						if (i < sqlSize)
							System.out.println(Statement.EXECUTE_FAILED
									+ "------error sql " + i + " = "
									+ cacheSqlList.get(i));
					}
				}
				System.out
						.println("-------------error sql end-------------------------");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ConnectSql.push(conn);

		}
		lastaddSqlTime = new Date().getTime();
	}

	/**
	 * 将临时表数据导入存储表
	 */
	protected void tmpToData() {
		if (this.srcTableName != null && filedsStr != null) {
			String sql = "insert ignore into " + srcTableName + "(" + filedsStr
					+ ") select " + filedsStr + " from " + tableName;
			Connection conn = ConnectSql.getConn();
			try {
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(sql);

				sql = "drop table IF EXISTS "
						+ TMP_DATABASE_NAME
						+ "."
						+ srcTableName.replace(".", "_")
						+ "_"
						+ new SimpleDateFormat("yyyyMMdd").format(new Date(
								this.createrTableObjDate.getTime() - 3 * 24
										* 3600 * 1000));
				stmt.executeUpdate(sql);
				stmt.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ConnectSql.push(conn);
		}
	}

	/**
	 * 数据插入
	 * 
	 * 定义成final禁止被子类重写
	 * 
	 * @param data
	 */
	public final synchronized void insertData(
			LinkedList<HashMap<String, String>> dataList, Url ui) {
		HashMap<String, LinkedList<String>> data = this.conectionData(dataList,
				ui);
		List<HashMap<String, String>> listData = this.listData(data);
		// 生成部分sql
		// 程序有数据插入后启动内部线程，扫描数据插入情况
		if (!isRun) {
			isRun = true;
			this.setInspectInformation(this.ci);
			new Thread(new TimeExecInsert()).start();
		}
		List<String> insertSqlList = insertDataSql(listData);
		addSql(insertSqlList);

	}

	/**
	 * 整理数据
	 */
	@SuppressWarnings("deprecation")
	public HashMap<String, LinkedList<String>> conectionData(
			LinkedList<HashMap<String, String>> dataList, Url ui) {
		HashMap<String, LinkedList<String>> tableFiledData = new HashMap<String, LinkedList<String>>();

		for (Iterator<Entry<String, HashMap<String, String>>> iter = tableInfo
				.entrySet().iterator(); iter.hasNext();) {
			Entry<String, HashMap<String, String>> e = iter.next();
			HashMap<String, String> filedInfo = e.getValue();
			// 取值方式
			int dataType = Integer.parseInt(filedInfo.get("filed_data_type"));
			String colName = e.getKey();
			if (!tableFiledData.containsKey(colName))
				tableFiledData.put(colName, new LinkedList<String>());
			LinkedList<String> colList = tableFiledData.get(colName);
			/**
			 * dataType 对应表data_table_filed_list.filed_data_type字段 1-取匹配值
			 * 2-取当前访问地址保存参数 3-取匹配序号 4-取匹配合计数 5-取当前地址
			 */
			if (dataType == 1 || dataType == 4) {
				for (int d = 0; d < dataList.size(); d++) {
					if (dataList.get(d).containsKey(colName))
						colList.add((dataList.get(d)).get(colName));
				}
				if (dataType == 4) {
					int dataNum = colList.size();
					colList.clear();
					colList.add(dataNum + ""); // 计数
				}
			} else if (dataType == 2)
				colList.add(ui.getData(colName));
			else if (dataType == 6) {
				colList.add(new Date().getHours() + "");
			} else if (dataType == 7) {
				if (new Date().getMinutes() >= 30) {
					colList.add("45");
				} else {
					colList.add("15");
				}

			} else if (dataType == 3)
				colList.add("{---pNum---}");
			else if (dataType == 5)
				colList.add(ui.getUrl());
			// 数据处理方法
			String dataFunName = filedInfo.get("filed_data_fun") != null ? filedInfo
					.get("filed_data_fun").trim() : null;
			if (dataFunName != null && !dataFunName.equals("")) {
				Method method = null;
				try {
					method = this.getClass().getMethod(dataFunName,
							String.class);
					Class<?> reClass = method.getReturnType();
					if (!reClass.isInstance(new String()))
						method = null;

					if (method != null) {
						int colDataLength = colList.size();
						for (int i = 0; i < colDataLength; i++) {
							colList.set(i, (String) method.invoke(this,
									colList.get(i)));
						}
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}

		return tableFiledData;
	}

	/**
	 * 数据平行处理
	 * 
	 * @param tableData
	 * @return
	 */
	private synchronized List<HashMap<String, String>> listData(
			HashMap<String, LinkedList<String>> data) {
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		boolean isWhile = true;
		int i = 0;
		while (isWhile) {
			HashMap<String, String> rowData = new HashMap<String, String>();
			boolean isHaveData = false;
			for (Iterator<Entry<String, HashMap<String, String>>> iter = tableInfo
					.entrySet().iterator(); iter.hasNext();) {
				Entry<String, HashMap<String, String>> entry = iter.next();
				String colName = entry.getKey();
				HashMap<String, String> filedInfo = entry.getValue();
				String colValue = "";
				if (data.get(colName).size() - 1 < i) {
					if (i == 0
							&& (filedInfo.get("default_value") == null || filedInfo
									.get("default_value").trim().equals(""))) {
						isWhile = false;
						break;
					} else {
						if (data.get(colName).size() != 0)
							colValue = data.get(colName).get(
									data.get(colName).size() - 1);
						else
							colValue = "";
					}
				} else {
					isHaveData = true;
					colValue = data.get(colName).get(i);
				}

				// 对值进行处理
				if (colValue != null && colValue.equals("{---pNum---}"))
					colValue = i + "";// 匹配序号
				else if (colValue != null && !colValue.equals(""))
					colValue = colValue.replace("'", "''")
							.replace("\\", "\\\\");// 去除单引号及转义反
				else if (colValue == null || colValue.equals(""))
					colValue = filedInfo.get("default_value");// 默认值
				// 值为null时赋值为空字符串
				if (colValue == null)
					colValue = "";

				rowData.put(colName, colValue);
			}
			if (!isWhile || !isHaveData)
				break;
			dataList.add(rowData);
			i++;
		}
		return dataList;
	}

	// 去除unicode编码
	public String replaceUnicode(String colValue) {
		if (colValue != null && !colValue.equals("")) {
			Matcher m = replaceUnicodePattern.matcher(colValue);
			if (m.find()) {
				colValue = Base64.decodeUnicode(colValue.replaceAll(
						"\\\\u(?=[0-9a-fA-F]{4})", "&#x"));
			}
		}
		return colValue;
	}

	/**
	 * 设置检查重复检查的字段
	 * 
	 * @param ci
	 */
	protected void setInspectInformation(InspectInformation ci) {

	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 生成插入数据sql
	 * 
	 * @param data
	 * @return
	 */
	protected List<String> insertDataSql(List<HashMap<String, String>> data) {
		List<String> sqlList = new ArrayList<String>();
		// 初始化sql语句前部分
		if (this.insertSqlBefor == null) {
			int i = 0;
			for (Iterator<Entry<String, HashMap<String, String>>> iter = tableInfo
					.entrySet().iterator(); iter.hasNext();) {
				String colName = iter.next().getKey();
				if (i != 0)
					filedsStr += ",";
				filedsStr += colName;
				i++;
			}
			insertSqlBefor = "insert ignore into " + tableName + "("
					+ filedsStr + ")";
		}
		// 生成具体sql语句
		int i = 0;
		for (i = 0; i < data.size(); i++) {
			String insertSql = this.insertSqlBefor + " values";
			HashMap<String, String> rowData = data.get(i);
			insertSql += "(";
			int j = 0;
			for (Iterator<Entry<String, HashMap<String, String>>> iter = tableInfo
					.entrySet().iterator(); iter.hasNext();) {
				Entry<String, HashMap<String, String>> entry = iter.next();
				String colName = entry.getKey();
				String colValue = replaceBlank(rowData.get(colName));
				if (j != 0)
					insertSql += ",";
				insertSql += colValue.equals("NOW()") ? colValue : "'"
						+ colValue + "'";
				j++;
			}
			insertSql += ")";
			sqlList.add(insertSql);
		}
		return sqlList;
	}

	public String replaceISO(String colValue) {
		return StringEscapeUtils.unescapeHtml(StringEscapeUtils
				.unescapeHtml(colValue));
	}

	/**
	 * 检测最后插入时间，看是否需要执行插入
	 * 
	 */
	class TimeExecInsert implements Runnable {
		@Override
		public void run() {
			while (isRun) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (((new Date().getTime() - lastaddSqlTime) >= maxInsertTime && maxInsertTime != 0)
						|| sqlList.size() > chacheSqlLength) {
					LinkedList<String> exeList;
					System.err
							.println("time inspect = "
									+ ((new Date().getTime() - lastaddSqlTime) >= maxInsertTime)
									+ " size Inspect="
									+ (sqlList.size() > chacheSqlLength));
					synchronized (sqlList) {
						long bTime = System.currentTimeMillis();
						exeList = new LinkedList<String>(sqlList);
						sqlList.clear();
						long eTime = System.currentTimeMillis();
						System.out.println("put data time.................."
								+ (eTime - bTime));
					}
					if (exeList != null) {
						execSqlBatch(exeList);
					}
				}
			}
		}
	}

	public String subStr260(String colValue) {
		int lastIndex = 260;
		if (colValue != null && !colValue.equals("")) {
			if (colValue.length() > lastIndex)
				colValue = colValue.substring(0, lastIndex);
		}
		return colValue;
	}
}
