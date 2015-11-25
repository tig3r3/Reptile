package com.syntun.webget;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList; //import java.util.regex.Pattern;
import java.util.List;
import java.util.UUID;

import com.syntun.putdata.InsertData; //import com.syntun.tools.Base64;
import com.syntun.tools.ConnectSql;
import com.syntun.tools.GetMD5;
import com.syntun.tools.SleepUtil;
import com.syntun.wetget.annotation.SetProperty;

@SetProperty(propertyVars = { "INSERT_EXE_NUM", "MAX_CACHE_NUM",
		"maxTryGetNum", "tryGetUrlTime", "mysqlBinDir", "isFirst", "isUpload" }, propertyComment = "抓取地址保存设置")
public class UrlSupervise {
	// private String mysqlBinDir =
	// "C:\\Program Files\\MySQL\\MySQL Server 5.5\\bin";
	// private String mysqlBinDir = "/usr/local/mysql/bin/";
	public static String mysqlBinDir = "/usr/bin/";
	public static int isUpload = 1;

	/**
	 * 当集合中保存的url_info数量达到该值时开始插入
	 */
	private static int INSERT_EXE_NUM = 1000;
	/**
	 * 最大缓存URLINFO数量
	 */
	private static int MAX_CACHE_NUM = 3000;
	/**
	 * 最大入库间隔毫秒数
	 */
	private static final int MAX_INSERT_TIME = 120 * 1000;
	/**
	 * 无地址插入确认时间
	 */
	private static final int NO_INSERT_TIME = MAX_INSERT_TIME / 1000 * 10;
	/**
	 * 控制是否插入
	 */
	private static boolean isInsert = false;
	/**
	 * 最后一次插入数据库时间
	 */
	private static long lastInsertTime = new Date().getTime();
	/**
	 * 最后一次查询时间
	 */
	// private static long lastSelectTime = new Date().getTime();
	/**
	 * 当待抓取UrlInfo列表中(getUrlList)数量小于该值时开始执行查询
	 */
	public static final int MIN_SELECT_NUM = 300;
	/**
	 * 每次执行查询时limit的条数
	 */
	public static int SELECT_LIMIT_NUM = 400;
	/**
	 * 待抓取取列表
	 */
	private static LinkedList<Url> getUrlList = new LinkedList<Url>();
	/**
	 * 待入库列表
	 */
	private static LinkedList<Url> insertUrlList = new LinkedList<Url>();
	/**
	 * 本机ip
	 */
	public static String localhostIp = "1";
	/**
	 * 尝试抓取次数
	 */
	public static int maxTryGetNum = 3;
	/**
	 * 尝试重新抓取时间间隔（分钟数）
	 */
	public static int tryGetUrlTime = 1;

	/**
	 * 更改单品页入库状态
	 */
	private static LinkedList<ProductUrl> productUrlList = new LinkedList<ProductUrl>();

	/**
	 * 私有化构造方法
	 */
	private UrlSupervise() {
		Connection conn = ConnectSql.getConn();
		try {
			Statement stmt = conn.createStatement();
			// 加载地址过滤正则
			String sql = "select url_group,sort_id from product_url";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ProductUrl p = new ProductUrl();
				p.setUrlGroup(rs.getInt("url_group"));
				p.setSortId(rs.getString("sort_id"));
				productUrlList.add(p);
			}
			stmt.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.exit(0);
		}
		ConnectSql.push(conn);
	}

	/**
	 * 启动抓取地址信息线程管理
	 */
	public static void startGeWebsite() {
		// new Thread(new GetTmallCookie()).start();
		ContentManager.startAddContent();
		UrlSupervise gl = new UrlSupervise();
		new Thread(gl.new insertUrl()).start();
		new Thread(gl.new getUrl()).start();
		UrlSupervise.resetGeter(0);
		new Thread(gl.new inspectGeterStatus()).start();
		UrlLimitInit.init();
		// new Thread(new UserDangdang()).start();
	}

	/**
	 * 清空缓存的地址并入库
	 */
	public static void clearUrlInfos() {
		synchronized (UrlSupervise.insertUrlList) {
			LinkedList<Url> il = new LinkedList<Url>(UrlSupervise.insertUrlList);

			UrlSupervise.execInsertUrl(il);

		}

	}

	/**
	 * 初始化静态代码块
	 */
	static {
		// 获取ip
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			boolean isfined = false;
			while (netInterfaces.hasMoreElements() && !isfined) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ei = ni.getInetAddresses();
				while (ei.hasMoreElements()) {
					InetAddress ia = ei.nextElement();
					String[] localhostIpArr = ia.getHostAddress().split("\\.");
					if (localhostIpArr.length == 4
							&& (localhostIpArr[0] + localhostIpArr[1])
									.equals("192168")) {
						localhostIp = localhostIpArr[3];
						isfined = true;
						break;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * 获取抓取地址
	 * 
	 * @return UrlInfo
	 */
	public static Url getUrlInfo() {
		synchronized (getUrlList) {
			if (getUrlList.size() > 0)
				return getUrlList.pollFirst();
			else {
				UrlSupervise.isInsert = true;
				return null;
			}
		}
	}

	/**
	 * 添加待抓取地址
	 * 
	 * @return UrlInfo
	 */
	public static void addParseUrlInfo(Url ui) {
		synchronized (getUrlList) {
			getUrlList.add(ui);
		}
	}

	/**
	 * 添加待抓取url地址到url集合的第一个
	 * 
	 * @return UrlInfo
	 */
	public static void addParseUserUrlInfo(Url ui) {
		synchronized (getUrlList) {
			getUrlList.addFirst(ui);
		}
	}

	/**
	 * 添加抓取地址
	 * 
	 * @return boolean
	 */
	public static boolean addUrlInfo(Url ui) {
		boolean status = ui.isStatus();
		synchronized (insertUrlList) {
			ui = handleUrlString(ui);
			if (ui.getPatternUrlId() == 3000 && ui.getSortId() == 201) {
				ui.setUrl(ui.getUrl().replace("?1=1", ""));
			}
			while (insertUrlList.size() >= MAX_CACHE_NUM && !status) {
				try {
					System.out.println("wait sleep add url ...");
					insertUrlList.wait(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
			if (ui != null)
				insertUrlList.add(ui);
			return true;
		}
	}

	/**
	 * 添加抓取地址
	 * 
	 * @return boolean
	 */
	public static boolean addUrlInfo(List<Url> listUrl) {
		synchronized (insertUrlList) {
			while (insertUrlList.size() >= MAX_CACHE_NUM) {
				try {
					System.out.println("wait sleep add url ...");
					insertUrlList.wait(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
			insertUrlList.addAll(listUrl);
			return true;
		}
	}

	/**
	 * 对添加的地址进行处理
	 * 
	 * @param ui
	 * @return UrlInfo
	 */
	public static synchronized Url handleUrlString(Url urlInfo) {
		String url = urlInfo.getUrl();
		try {
			if (url == null || url.equals("")) {
				System.out.println("获取到地址不能为空");
				System.exit(0);
				return null;
			}
			// 过滤#号
			// url = url.trim().replaceAll("#","%23").replaceAll(" ", "%20");
			/**
			 * if(urlFilterStr.containsKey(urlInfo.getPatternUrlId())){ String[]
			 * filterInfo = urlFilterStr.get(urlInfo.getPatternUrlId()); url =
			 * url.trim().replace(filterInfo[0], filterInfo[1]); }
			 */
			// 取绝对路径和获取host
			if (url.indexOf("http://") != 0&&url.indexOf("https://") != 0) {
				URL absUrl = new URL(new URL(urlInfo.getPreantUrl()), url);
				url = absUrl.toExternalForm();
			}
			urlInfo.setUrl(url);
//			boolean isPally = true;
//			/**
//			 * int patternUrlId = urlInfo.getPatternUrlId();
//			 * if(urlFilter.containsKey(patternUrlId)){ for(int
//			 * i=0;i<urlFilter.get(patternUrlId).size();i++) { String
//			 * urlfilterStr = new String(urlFilter.get(patternUrlId).get(i)[0]);
//			 * if(Pattern.matches(urlfilterStr, url)){ isPally = true; break; }
//			 * } } else isPally = true;
//			 */
//			if (!isPally)
//				urlInfo = null;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return urlInfo;
	}

	/**
	 * 插入到数据库
	 */
	public synchronized static void execInsertUrl(LinkedList<Url> li) {
		long startInsert = System.currentTimeMillis();
		String uuid = UUID.randomUUID().toString();
		// 插入内存表sql
		String insertMsql = "insert ignore into url_status(add_time,url_md5,insert_batch_num,is_parse_over,sort_id) values(NOW(),?,'"
				+ uuid + "',?,?)";
		// 插入硬盘表sql
		String insertDsql = "insert ignore into url_list(url_md5,url_str,father_url,url_group,url_data,id,url_charset,sort_id) "
				+ "values(?,?,?,?,?,?,?,?)";
		Connection conn = ConnectSql.getConn();
		GetMD5 gmd5 = new GetMD5();
		try {
			// 插入内存表
			PreparedStatement pstmt = conn.prepareStatement(insertMsql);
			// pstmt = conn.prepareStatement(upMsql);
			// pstmt.executeUpdate("lock tables get_url_status write;");
			int batchNum = 0;
			for (int i = 0; i < li.size(); i++) {
				Url urlInfoObj = li.get(i);
				String md5Code = gmd5.mD5Code(urlInfoObj.getUrl() + ":"
						+ urlInfoObj.getPatternUrlId());
				pstmt.setString(1, md5Code);
				// 判断是否是访问限制的sort_id
				boolean isLimit = true;
				for (UrlLimit ul : UrlLimitInit.urlLimitList) {
					if (ul.getUrlGroup().equals(
							urlInfoObj.getPatternUrlId() + "")) {
						for (String s : ul.getSortIdList()) {
							if (s.equals(urlInfoObj.getSortId() + "")) {
								isLimit = false;
								pstmt.setInt(2, ul.getStatus());
								break;
							}
						}
					}
				}

				if (isLimit) {
					boolean status = false;
					// 判断是否为本次解析不解析的url地址
					for (ProductUrl productUrl : productUrlList) {
						if ((urlInfoObj.getSortId() + "").equals(productUrl
								.getSortId())
								&& urlInfoObj.getPatternUrlId() == productUrl
										.getUrlGroup()) {
							status = true;
						}
					}
					if (status == true) {
						pstmt.setInt(2, 2);
					} else {
						pstmt.setInt(2, 0);
					}
				}
				pstmt.setInt(3, urlInfoObj.getSortId());
				pstmt.addBatch();
				batchNum++;
				if (batchNum % 100 == 0) {
					pstmt.executeBatch();
					batchNum = 0;
				}
			}
			if (batchNum != 0) {
				pstmt.executeBatch();
				pstmt.clearBatch();
				batchNum = 0;
			}
			// 查询出成功插入的数据
			HashMap<String, Integer> insertMd5Hm = new HashMap<String, Integer>();
			ResultSet rs = pstmt
					.executeQuery("select id,url_md5 from url_status where insert_batch_num='"
							+ uuid + "'");

			while (rs.next()) {
				insertMd5Hm.put(rs.getString("url_md5"), rs.getInt("id"));
			}
			pstmt.close();
			// 插入硬盘表
			pstmt = conn.prepareStatement(insertDsql);
			for (int i = 0; i < li.size(); i++) {
				Url urlInfoObj = li.get(i);
				String md5Code = gmd5.mD5Code(urlInfoObj.getUrl() + ":"
						+ urlInfoObj.getPatternUrlId());
				if (insertMd5Hm.containsKey(md5Code)) {
					int insertId = insertMd5Hm.get(md5Code);
					pstmt.setString(1, md5Code);
					pstmt.setString(2, urlInfoObj.getUrl());
					pstmt.setString(3, urlInfoObj.getPreantUrl());
					pstmt.setInt(4, urlInfoObj.getPatternUrlId());
					pstmt.setString(5, urlInfoObj.getDataStr());
					pstmt.setInt(6, insertId);
					pstmt.setString(7, urlInfoObj.getChartSet());
					pstmt.setInt(8, urlInfoObj.getSortId());
					pstmt.addBatch();
					batchNum++;
					if (batchNum % 100 == 0) {
						pstmt.executeBatch();
						pstmt.clearBatch();
						pstmt.clearParameters();
						batchNum = 0;
					}
				}
			}
			if (batchNum != 0) {
				pstmt.executeBatch();
			}
			pstmt.close();
			ConnectSql.push(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("插入地址错误");
			System.exit(0);
		}
		System.out.println("插入url使用时间："
				+ (System.currentTimeMillis() - startInsert));
	}

	// /**
	// * 插入到数据库
	// */
	// public synchronized static void execInsertUrl(LinkedList<Url> li) {
	// long startInsert = System.currentTimeMillis();
	// String uuid = UUID.randomUUID().toString();
	// Connection conn = ConnectSql.getConn();
	// GetMD5 gmd5 = new GetMD5();
	// String douhao = "','";
	// try {
	// // 插入内存表
	// StringBuffer sbUrlStatus = new StringBuffer();
	// for (int i = 0; i < li.size(); i++) {
	// Url urlInfoObj = li.get(i);
	// String md5Code = gmd5.mD5Code(urlInfoObj.getUrl() + ":"
	// + urlInfoObj.getPatternUrlId());
	// int status = 0;
	// for (ProductUrl productUrl : productUrlList) {
	// if ((urlInfoObj.getSortId() + "").equals(productUrl
	// .getSortId())
	// && urlInfoObj.getPatternUrlId() == productUrl
	// .getUrlGroup()) {
	// status = 2;
	// }
	// }
	// if (sbUrlStatus.length() == 0) {
	// sbUrlStatus
	// .append("insert ignore into url_status(add_time,url_md5,insert_batch_num,is_parse_over,sort_id) values");
	// } else {
	// sbUrlStatus.append(",");
	// }
	// sbUrlStatus.append("(NOW(),'").append(md5Code).append(douhao)
	// .append(uuid).append(douhao).append(status)
	// .append(douhao).append(urlInfoObj.getSortId() + "')");
	// }
	//
	// Statement pstmt = conn.createStatement();
	// if (sbUrlStatus.length() == 0) {
	// return;
	// }
	// pstmt.execute(sbUrlStatus.toString());
	// pstmt.close();
	// Statement pstmt1 = conn.createStatement();
	// // 查询出成功插入的数据
	// HashMap<String, Integer> insertMd5Hm = new HashMap<String, Integer>();
	// ResultSet rs = pstmt1
	// .executeQuery("select id,url_md5 from url_status where insert_batch_num='"
	// + uuid + "'");
	// while (rs.next()) {
	// insertMd5Hm.put(rs.getString("url_md5"), rs.getInt("id"));
	// }
	// // 插入硬盘表
	// StringBuffer sbUrlList = new StringBuffer();
	// for (int i = 0; i < li.size(); i++) {
	// Url urlInfoObj = li.get(i);
	// String md5Code = gmd5.mD5Code(urlInfoObj.getUrl() + ":"
	// + urlInfoObj.getPatternUrlId());
	// if (insertMd5Hm.containsKey(md5Code)) {
	// int insertId = insertMd5Hm.get(md5Code);
	// if (sbUrlList.length() == 0) {
	// sbUrlList
	// .append("insert ignore into url_list(url_md5,url_str,father_url,url_group,url_data,id,url_charset,sort_id) values ");
	// } else {
	// sbUrlList.append(",");
	// }
	// sbUrlList
	// .append("('")
	// .append(md5Code)
	// .append(douhao)
	// .append(urlInfoObj.getUrl())
	// .append(douhao)
	// .append(urlInfoObj.getPreantUrl())
	// .append(douhao)
	// .append(urlInfoObj.getPatternUrlId())
	// .append(douhao)
	// .append(StringEscapeUtils.escapeSql(urlInfoObj
	// .getDataStr())).append(douhao)
	// .append(insertId).append(douhao)
	// .append(urlInfoObj.getChartSet()).append(douhao)
	// .append(urlInfoObj.getSortId()).append("')");
	// }
	// }
	// Statement pstmt2 = conn.createStatement();
	// if (sbUrlList.length() > 0) {
	// pstmt2.execute(sbUrlList.toString());
	// }
	// pstmt2.close();
	// ConnectSql.push(conn);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// System.out.println("插入地址错误");
	// System.exit(0);
	// }
	// System.out.println("插入url使用时间："
	// + (System.currentTimeMillis() - startInsert));
	// }

	// /**
	// * 插入到数据库
	// */
	// public synchronized static void execInsertUrl(LinkedList<Url> li) {
	// long startInsert = System.currentTimeMillis();
	// String uuid = UUID.randomUUID().toString();
	// Connection conn = ConnectSql.getConn();
	// GetMD5 gmd5 = new GetMD5();
	// String douhao = ",";
	// try {
	// // 插入内存表
	// StringBuffer sbUrlStatus = new StringBuffer();
	// String sql = "load data local infile '' "
	// +
	// " ignore into table url_status (add_time,url_md5,insert_batch_num,is_parse_over,sort_id)FIELDS terminated by ','";
	// PreparedStatement pst = conn.prepareStatement(sql);
	// for (int i = 0; i < li.size(); i++) {
	// Url urlInfoObj = li.get(i);
	// String md5Code = gmd5.mD5Code(urlInfoObj.getUrl() + ":"
	// + urlInfoObj.getPatternUrlId());
	// int status = 0;
	// for (ProductUrl productUrl : productUrlList) {
	// if ((urlInfoObj.getSortId() + "").equals(productUrl
	// .getSortId())
	// && urlInfoObj.getPatternUrlId() == productUrl
	// .getUrlGroup()) {
	// status = 2;
	// }
	// }
	// sbUrlStatus.append("NOW(),").append(md5Code).append(douhao)
	// .append(uuid).append(douhao).append(status)
	// .append(douhao).append(urlInfoObj.getSortId() + "\n");
	// }
	// InputStream is = new ByteArrayInputStream(sbUrlStatus.toString()
	// .getBytes());
	// ((com.mysql.jdbc.Statement) pst).setLocalInfileInputStream(is);
	// pst.execute();
	// conn.commit();
	//
	// if (sbUrlStatus.length() == 0) {
	// return;
	// }
	// Statement pstmt1 = conn.createStatement();
	// // 查询出成功插入的数据
	// HashMap<String, Integer> insertMd5Hm = new HashMap<String, Integer>();
	// ResultSet rs = pstmt1
	// .executeQuery("select id,url_md5 from url_status where insert_batch_num='"
	// + uuid + "'");
	// while (rs.next()) {
	// insertMd5Hm.put(rs.getString("url_md5"), rs.getInt("id"));
	// }
	//
	// String sql1 = "load data local infile '' "
	// +
	// " ignore into table url_list (url_md5,url_str,father_url,url_group,url_data,id,url_charset,sort_id)FIELDS terminated by ','";
	// PreparedStatement pst1 = conn.prepareStatement(sql1);
	//
	// // 插入硬盘表
	// StringBuffer sbUrlList = new StringBuffer();
	// for (int i = 0; i < li.size(); i++) {
	// Url urlInfoObj = li.get(i);
	// String md5Code = gmd5.mD5Code(urlInfoObj.getUrl() + ":"
	// + urlInfoObj.getPatternUrlId());
	// if (insertMd5Hm.containsKey(md5Code)) {
	// int insertId = insertMd5Hm.get(md5Code);
	// sbUrlList
	// .append(md5Code)
	// .append(douhao)
	// .append(urlInfoObj.getUrl())
	// .append(douhao)
	// .append(urlInfoObj.getPreantUrl())
	// .append(douhao)
	// .append(urlInfoObj.getPatternUrlId())
	// .append(douhao)
	// .append(StringEscapeUtils.escapeSql(urlInfoObj
	// .getDataStr())).append(douhao)
	// .append(insertId).append(douhao)
	// .append(urlInfoObj.getChartSet()).append(douhao)
	// .append(urlInfoObj.getSortId()).append("\n");
	// }
	// }
	// InputStream is1 = new ByteArrayInputStream(sbUrlList.toString()
	// .getBytes());
	// ((com.mysql.jdbc.Statement) pst1).setLocalInfileInputStream(is1);
	// pst1.execute();
	// conn.commit();
	// ConnectSql.push(conn);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// System.out.println("插入地址错误");
	// System.exit(0);
	// }
	// System.out.println("插入url使用时间："
	// + (System.currentTimeMillis() - startInsert));
	// }

	/**
	 * 查询待抓取地址
	 */
	private synchronized static void selectUrlInfo() {
//		String sql = "SELECT b.* FROM (SELECT id FROM `url_status` WHERE is_parse_over=0 and try_num<"
//				+ maxTryGetNum
//				+ " and get_time<date_sub(now(), interval "
//				+ tryGetUrlTime
//				+ " minute) ORDER BY url_md5 LIMIT 0,"
//				+ SELECT_LIMIT_NUM
//				+ ") a JOIN `url_list` b ON(a.id=b.id)";
		
		String sql = "SELECT b.* FROM (SELECT id FROM `url_status` WHERE is_parse_over=0 and try_num<"
				+ maxTryGetNum
				+ " and get_time<date_sub(now(), interval "
				+ tryGetUrlTime
				+ " minute) LIMIT 0,"
				+ SELECT_LIMIT_NUM
				+ ") a JOIN `url_list` b ON(a.id=b.id)";
		Connection conn = ConnectSql.getConn();
		try {
			Statement tmt = conn.createStatement();
			ResultSet rs = tmt.executeQuery(sql);

			String ids = "0";
			synchronized (getUrlList) {
				while (rs.next()) {
					String host = new URL(rs.getString("url_str")).getHost()
							.toLowerCase();
					Url ui = new Url(rs.getString("url_str"),
							rs.getString("father_url"), rs.getInt("sort_id"));
					ui.setHost(host);
					ui.addData(rs.getString("url_data"));
					ui.setGetUrlId(rs.getInt("id"));
					ui.setUrlId(rs.getInt("id"));
					ui.setPatternUrlId(rs.getInt("url_group"));
					ui.setChartSet(rs.getString("url_charset"));
					ui.setPreantUrl(rs.getString("father_url"));
					ids += "," + rs.getInt("id");
					UrlSupervise.getUrlList.add(ui);
				}
				rs.close();
			}
			sql = "update url_status set get_time=now(),try_num=try_num+1 where id in ("
					+ ids + ")";
			tmt.executeUpdate(sql);
			tmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取地址错误！程序终止");
			System.exit(0);
		}
		ConnectSql.push(conn);
	}

	/**
	 * 检查本轮抓取是否已完成，完成清空地址列表
	 * 
	 * 检测条件
	 * 
	 * 1.100倍最大插入的时间间隔内没有新的地址添加（UrlInfoManger.MAX_INSERT_TIME） 2.所有地址已经完成解析
	 */
	private synchronized static void inspectAllGeterStatus() {
		String sql = "select UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(max(add_time))>"
				+ NO_INSERT_TIME
				+ ",SUM(IF(is_parse_over=1 || try_num>=3,1,0))=COUNT(1),count(1) from url_status";
		Connection conn = ConnectSql.getConn();
		try {
			Statement tmt = conn.createStatement();
			ResultSet rs = tmt.executeQuery(sql);
			boolean isOutTime = false;
			boolean isParseAll = false;
			long getUrlNum = 0;
			if (rs.next()) {
				isOutTime = rs.getBoolean(1);
				isParseAll = rs.getBoolean(2);
				getUrlNum = rs.getLong(3);
			}
			if (isOutTime && isParseAll) {
				UrlSupervise.resetGeter(getUrlNum);
				Thread.sleep(UrlSupervise.MAX_INSERT_TIME * 2);
			}
			tmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
	}

	public static void main(String[] args) {
		inspectGeterStatus();
	}

	/**
	 * 重起抓取
	 */
	private synchronized static String resetGeter(long getUrlNum) {
		// 加载站点列表 init_url_list
		String sql = "select id,url_str,url_group,url_data,url_charset,sort_id from init_url_list where url_status=1";
		// 清空状态表
		String trunceStatusSql = "TRUNCATE TABLE `url_status`";
		// 清空地址列表
		String trunceListSql = "TRUNCATE TABLE `url_list`";

		Connection conn = ConnectSql.getConn();
		String patternUrlIds = "0";
		try {
			Statement stmt = conn.createStatement();
			// 清空
			if (getUrlNum != 0) {
				// stmt.execute("DELETE FROM wgdata.url_list WHERE sort_id = '300'");
				// stmt.execute("DELETE FROM wgdata.url_status WHERE id  NOT IN (SELECT id FROM wgdata.url_list)");
				// stmt.executeUpdate("UPDATE wgdata.url_status SET `get_time` = '0000-00-00 00:00:00', `parse_time` = '0000-00-00 00:00:00', `is_parse_over` = '0', `try_num` = '0'");
				stmt.executeUpdate(trunceListSql);
				stmt.executeUpdate(trunceStatusSql);
			}
			// 重置
			else {
				InsertData.resetInsertData();
				ResolveWebPage.resetParse();
			}
			// 重新加载首地址
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String gUrl = rs.getString("url_str");
//				if (gUrl != null
//						&& rs.getString("url_str").indexOf("http://") == -1
//						&& rs.getString("url_str").indexOf("https://") == -1)
//					gUrl = "http://" + rs.getString("url_str") + "/";
				Url ui = new Url(gUrl, 0);
				ui.setHostId(rs.getInt("id"));
				ui.addData(rs.getString("url_data"));
				ui.setPatternUrlId(rs.getInt("url_group"));
				ui.setChartSet(rs.getString("url_charset"));
				ui.setSortId(rs.getInt("sort_id"));
				ui.setStatus(true);
				if (gUrl != null)
					UrlSupervise.addUrlInfo(ui);
				patternUrlIds += "," + rs.getInt("url_group");
			}
			stmt.close();
			PreparedStatement ps = conn
					.prepareStatement("INSERT INTO run_list(`host`, `get_num`)VALUES ('"
							+ localhostIp + "', '" + getUrlNum + "');");
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
		return patternUrlIds;
	}

	/**
	 * 入库线程对象
	 */
	class insertUrl implements Runnable {
		@Override
		public void run() {
			while (true) {
				SleepUtil.sleep(200);
				LinkedList<Url> il = null;
				synchronized (UrlSupervise.insertUrlList) {
					if (UrlSupervise.insertUrlList.size() >= UrlSupervise.INSERT_EXE_NUM
							|| (UrlSupervise.insertUrlList.size() > 0 && (new Date()
									.getTime() - UrlSupervise.lastInsertTime) >= UrlSupervise.MAX_INSERT_TIME)
							|| UrlSupervise.isInsert) {
						// 设置标志变量停止插入，并重新声明
						UrlSupervise.isInsert = false;
						il = new LinkedList<Url>(UrlSupervise.insertUrlList);
						UrlSupervise.insertUrlList.clear();
						UrlSupervise.lastInsertTime = new Date().getTime();
					}
				}
				if (il != null) {
					UrlSupervise.execInsertUrl(il);
					il = null;
				}
			}
		}
	}

	/**
	 * 数据库提取线程对象
	 */
	class getUrl implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (UrlSupervise.getUrlList.size() <= UrlSupervise.MIN_SELECT_NUM) {
					UrlSupervise.selectUrlInfo();
				}
			}
		}
	}

	/**
	 * 检测更新状态
	 * 
	 */
	class inspectGeterStatus implements Runnable {

		@Override
		public void run() {
			while (true) {
				if (isFirst == 1) {
					UrlSupervise.inspectGeterStatus();
				} else {
					UrlSupervise.inspectAllGeterStatus();
				}
				SleepUtil.sleep(10 * 60 * 1000);
			}
		}

	}

	public static int isFirst = 2;

	// @SuppressWarnings("deprecation")
	// private static int day = new Date().getDate();

	public static void inspectGeterStatus() {
		String sql = "select UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(max(add_time))>"
				+ NO_INSERT_TIME
				+ ",(SELECT COUNT(1) FROM url_status WHERE is_parse_over=0 and try_num<3) tt,count(1) from url_status";
		Connection conn = ConnectSql.getConn();
		try {
			Statement tmt = conn.createStatement();
			ResultSet rs = tmt.executeQuery(sql);
			boolean isOutTime = false;
			long isParseAll = 0;
			long getUrlNum = 0;
			if (rs.next()) {
				isOutTime = rs.getBoolean(1);
				isParseAll = rs.getLong(2);
				getUrlNum = rs.getLong(3);
			}
			if (isOutTime && isParseAll == 0) {
				UrlSupervise.resetGeterT(getUrlNum);
				Thread.sleep(UrlSupervise.MAX_INSERT_TIME * 2);
			}
			tmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
	}

	// @SuppressWarnings("deprecation")
	private static void resetGeterT(long getUrlNum) {
		// 加载站点列表 init_url_list
		String sql = "select id,url_str,url_group,url_data,url_charset,sort_id from init_url_list where url_status=1";
		// 清空状态表
		String trunceStatusSql = "DELETE FROM url_status WHERE `is_parse_over` = 1 OR (`is_parse_over` = '0' AND `try_num` = '3')";
		// 清空地址列表
		String trunceListSql = "DELETE FROM url_list WHERE id IN(SELECT id FROM url_status WHERE `is_parse_over` = '1' OR (`is_parse_over` = '0' AND `try_num` = '3'))";
		// // 清空状态表
		// String trunceStatus = "TRUNCATE TABLE `url_status`";
		// // 清空地址列表
		// String trunceList = "TRUNCATE TABLE `url_list`";
		Connection conn = ConnectSql.getConn();
		try {
			Statement stmt = conn.createStatement();

			// // 清空
			// if (getUrlNum != 0 && new Date().getDate() != day) {
			// System.out
			// .println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~restart~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			// stmt.executeUpdate(trunceStatus);
			// stmt.executeUpdate(trunceList);
			// day = new Date().getDate();
			// } else
			if (getUrlNum != 0) {
				stmt.executeUpdate(trunceListSql);
				stmt.executeUpdate(trunceStatusSql);
			}
			// 重置
			else {
				InsertData.resetInsertData();
				ResolveWebPage.resetParse();
			}
			// 重新加载首地址
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String gUrl = rs.getString("url_str");
//				if (gUrl != null
//						&& rs.getString("url_str").indexOf("http://") == -1)
//					gUrl = "http://" + rs.getString("url_str") + "/";
				Url ui = new Url(gUrl, 0);
				ui.setHostId(rs.getInt("id"));
				ui.addData(rs.getString("url_data"));
				ui.setPatternUrlId(rs.getInt("url_group"));
				ui.setChartSet(rs.getString("url_charset"));
				ui.setSortId(rs.getInt("sort_id"));
				ui.setStatus(true);
				if (gUrl != null)
					UrlSupervise.addUrlInfo(ui);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
	}
}
