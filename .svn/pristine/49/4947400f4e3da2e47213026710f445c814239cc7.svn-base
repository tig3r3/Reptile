package com.syntun.webget;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import com.syntun.file.UrlToFile;
import com.syntun.tools.ConnectSql;
import com.syntun.tools.SleepUtil;
import com.syntun.tools.ZipTools;

public class ContentManager {
	/**
	 * 存储获取到内容的url集合
	 */
	public static LinkedList<Url> list = new LinkedList<Url>();

	public static List<HashMap<String, String>> contentPattern = new ArrayList<HashMap<String, String>>();

	static {
		// 查询表名称
		String sql = "SELECT url_group, sort_id FROM content_pattern";
		Connection conn = ConnectSql.getConn();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				HashMap<String, String> p = new HashMap<String, String>();
				p.put("url_group", rs.getString("url_group"));
				p.put("sort_id", rs.getString("sort_id"));
				contentPattern.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
	}

	/**
	 * Url峰值
	 */
	public static int contentNum = 100;

	public static int contentMax = 4000;
	/**
	 * 最后一次插入数据库时间
	 */
	public static long lastInsertTime = new Date().getTime();

	/**
	 * 最大入库间隔毫秒数
	 */
	public static final int MAX_INSERT_TIME = 120 * 1000;

	/**
	 * 日志
	 */
	static Logger log = Logger.getLogger(ContentManager.class);

	/**
	 * 启动插入线程
	 */
	public static void startAddContent() {
		new Thread(new addContent()).start();
	}

	public static boolean addContent(Url url) {
		synchronized (list) {
			if (list.size() < contentMax) {
				list.add(url);
				return true;
			} else {
				log.info("err%%%%%%%%%%%%%%%%%%=" + list.size());
				return false;
			}
		}
	}

	public static void excuteWriteFile(LinkedList<Url> exelist) {
		for (Url ui : exelist) {
			try {
				UrlToFile u = UrlToFile.getFileToDataBase(ui.getPatternUrlId()
						+ "_" + ui.getSortId());
				u.addSql(getValue(ui));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String getValue(Url ui) {
		StringBuffer sb = new StringBuffer();
		sb.append(ui.getPatternUrlId()).append("_").append(ui.getSortId())
				.append("<tuo>").append(ui.getDataStr()).append("<tuo>");
		if (UrlSupervise.isFirst != 0) {
			for (HashMap<String, String> m : contentPattern) {
				if (m.get("url_group").equals(ui.getPatternUrlId())
						&& m.get("sort_id").equals(ui.getSortId())) {
					System.out.println("出现了一次特殊空格处理");
					return sb.append(
							ZipTools.zip(ui.getHtmlContent().replace("	", "")
									.replace("   ", "").replace("  ", " ")))
							.toString();
				}
			}
			sb.append(ZipTools.zip(ui.getHtmlContent().replace("	", "")
					.replace(" ", "")));
		} else {
			sb.append(ui.getHtmlContent());
		}
		return sb.toString();
	}
}

/**
 * 插入已经获取到内容的url地址
 * 
 * @author tuoxing
 * 
 */
class addContent implements Runnable {
	@Override
	public void run() {
		while (true) {

			LinkedList<Url> exelist = null;
			synchronized (ContentManager.list) {
				if (ContentManager.list.size() >= ContentManager.contentNum
						|| (ContentManager.list.size() > 0 && (new Date()
								.getTime() - ContentManager.lastInsertTime) >= ContentManager.MAX_INSERT_TIME)) {
					ContentManager.log.info("input&&&&&&&&&&&&&&&&&&="
							+ ContentManager.list.size());
					exelist = new LinkedList<Url>(ContentManager.list);
					ContentManager.list.clear();
					ContentManager.lastInsertTime = new Date().getTime();
				}
			}
			if (null != exelist) {
				ContentManager.excuteWriteFile(exelist);
			}
			SleepUtil.sleep(100);
		}

	}

}