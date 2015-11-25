package com.syntun.replace;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.syntun.tools.ConnectSql;


public class Dreplace implements replaceParameter {

	/**
	 * 进行参数提换
	 */
	public LinkedList<ParameInfo> execParame(String exeParam) {
		String[] exeParamArr = exeParam.split("\\.");
		boolean isGet = false;
		LinkedList<ParameInfo> dl = new LinkedList<ParameInfo>();
		if(exeParamArr.length==3){
			String sql = "select "+exeParamArr[2]+" from "+exeParamArr[0]+"."+exeParamArr[1];
			Connection conn = ConnectSql.getConn();
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()) dl.add(new ParameInfo(exeParamArr[2]+"",rs.getString(1)+""));
				stmt.close();
				if(dl.size()>0)isGet = true;
			} catch (SQLException e){}
			ConnectSql.push(conn);
		}
		if(!isGet) return null;
		else return dl;
	}
	
	
	public static void main(String[] args){
		System.out.println(new Dreplace().execParame("wgdata_360buy.area_list.area_id"));
	}

}
