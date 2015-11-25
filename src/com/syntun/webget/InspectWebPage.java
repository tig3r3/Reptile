package com.syntun.webget;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.syntun.tools.ConnectSql;

public class InspectWebPage {

	private static HashMap<Integer, ArrayList<Map<String, String>>> hostInspectContentHs = new HashMap<Integer, ArrayList<Map<String, String>>>();
	private static InspectWebPage inspect = null;

	private InspectWebPage() {
		try {
			Connection conn = ConnectSql.getConn();
			Statement stmt = conn.createStatement();
			String sql = "select * from web_error";
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rmd = rs.getMetaData();
			int columnNum = rmd.getColumnCount();
			while (rs.next()) {
				ArrayList<Map<String, String>> errContent;
				if (hostInspectContentHs.containsKey(rs.getInt("url_group")))
					errContent = hostInspectContentHs.get(rs
							.getInt("url_group"));
				else
					errContent = new ArrayList<Map<String, String>>();
				Map<String, String> m = new HashMap<String, String>();
				for (int i = 1; i <= columnNum; i++) {
					m.put(rmd.getColumnName(i), rs.getString(i));
				}
				errContent.add(m);
				hostInspectContentHs.put(rs.getInt("url_group"), errContent);
			}
			stmt.close();
			ConnectSql.push(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static InspectWebPage getInspectObj() {
		if (inspect == null) {
			inspect = new InspectWebPage();
		}
		return inspect;
	}

	/**
	 * 返回结果
	 * 
	 * @param ui
	 * @return
	 */
	public int inspectPage(Url ui) {
		if (ui.getHtmlContent() == null) {
			System.out.println("获取内容为空");
			System.exit(0);
		}
		if (hostInspectContentHs.containsKey(ui.getPatternUrlId())) {
			ArrayList<Map<String, String>> errContent = hostInspectContentHs
					.get(ui.getPatternUrlId());
			int errSize = errContent.size();
			for (int i = 0; i < errSize; i++) {
				if (errContent.get(i) == null) {
					break;
				}
				if (ui.getPatternUrlId() == 500001
						&& ui.getHtmlContent().equals("")) {
					return 1;
				}
				if (ui.getPatternUrlId() == 5 || ui.getPatternUrlId() == 50
						|| ui.getPatternUrlId() == 500) {
					if (ui.getHtmlContent().replace("	", "").replace(" ", "")
							.indexOf("您访问的网页不存在") != -1) {
						return 1;
					}
				}
				if (ui.getHtmlContent().indexOf(
						errContent.get(i).get("err_content_str")) != -1) {
					System.out.println("err_content_str:"
							+ errContent.get(i).get("err_content_str"));
					return Integer.parseInt(errContent.get(i).get(
							"err_content_result"));
				}
			}
		}
		return 0;
	}
}
