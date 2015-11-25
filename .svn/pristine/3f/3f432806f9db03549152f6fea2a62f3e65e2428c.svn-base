package com.syntun.replace;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import com.syntun.tools.ConnectSql;


public class Sreplace implements replaceParameter {

	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		Connection conn =  ConnectSql.getConn();
		LinkedList<ParameInfo> ll = new LinkedList<ParameInfo>();
		try {
			ResultSet rs = conn.createStatement().executeQuery(exeParam);
			while(rs.next()) {
				String value = rs.getString(1);
				ll.add(new ParameInfo("sqlValue", value));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
		return ll;
	}

}
