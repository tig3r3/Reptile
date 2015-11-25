package com.syntun.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.syntun.webget.GetWebPage;
import com.syntun.webget.StatsCount;
import com.syntun.webget.StatsTime;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

@SuppressWarnings("unused")
public class ConnADSL {
	private static Date prevDate;

	public static StatsCount statsCount = new StatsCount();

	public static StatsTime statsTime = new StatsTime();

	private final static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static synchronized void addStatsCount() {
		if (statsCount.getStartTime() == null) {
			statsCount.setStartTime(new Date());
		}
		statsCount.setTotalNum(statsCount.getTotalNum() + 1);
		if (statsTime.getStartTime() != null) {
			java.sql.Connection con = ConnectSql.getConn();
			try {
				PreparedStatement pSt = con
						.prepareStatement("INSERT INTO Stats_time(proxy_user,`start_time`, `end_time`, `is_limit`)VALUES ('"
								+ GetWebPage.proxyUser
								+ "', '"
								+ sdf.format(statsTime.getStartTime())
								+ "', '"
								+ sdf.format(new Date()) + "', '是')");
				pSt.execute();
				pSt.close();
				ConnectSql.push(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			statsTime = new StatsTime();
		}
	}

	/**
	 * 链接adsl 代理
	 */
	public static synchronized boolean connAgent(int port) {
		if (statsCount.getTotalNum() != 0) {
			java.sql.Connection con = ConnectSql.getConn();
			try {
				PreparedStatement pSt = con
						.prepareStatement("INSERT INTO Stats_count( proxy_user,`start_time`, `end_time`, `total_num`) VALUES ('"
								+ GetWebPage.proxyUser
								+ "', '"
								+ sdf.format(statsCount.getStartTime())
								+ "', '"
								+ sdf.format(new Date())
								+ "', '"
								+ statsCount.getTotalNum() + "')");
				pSt.execute();
				pSt.close();
				ConnectSql.push(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			statsCount = new StatsCount();
			if (statsTime.getStartTime() == null) {
				statsTime.setStartTime(new Date());
			}
		}

		SleepUtil.sleep(6000);
		return true;
	}

	public static void main(String[] args) {
		SetDefaultProperty.loadProperty();
		addStatsCount();
		connAgent(4);
		addStatsCount();
	}

	/**
	 * 链接adsl linux
	 */
	public static synchronized boolean connADSL() {
		Date nowDate = new Date();
		if (prevDate != null) {
			long time = nowDate.getTime() - ConnADSL.prevDate.getTime();
			if (time < 1000 * 10 * 1)
				return false;
		}
		System.out.println("diao le!!!");
		ConnADSL.prevDate = nowDate;
		Process p;
		Process s;
		try {
			s = Runtime.getRuntime().exec("adsl-stop");
			s.waitFor();
			p = Runtime.getRuntime().exec("adsl-start");
			p.waitFor();
			s.destroy();
			p.destroy();
			Thread.sleep(2000);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
