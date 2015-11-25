package com.syntun.lihao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syntun.tools.ConnectSql;

public class DBConn {
	private static  String sqlName = "root";
	private static String sqlPassWord = "root";
	private static String sqlDbName = "wgdata";
	private static String sqlHost = "192.168.0.107";
	private static String sqlConStr = "jdbc:mysql://"+sqlHost+"/"+sqlDbName+"?user="+sqlName+"&password="+sqlPassWord+"&seUnicode=true&characterEncoding=UTF8";
	/**
	 * @return Connection
	 */
	public static Connection getConn(){
		  Connection conn=null;
		 try {
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection(sqlConStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	public static void closeConn(Connection conn){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获得数据库名
	 * @return List<String>
	 */
	public static List<String> getDatabase(){
		//Connection conn =ConnectSql.getConn();
		List<String> list=new ArrayList<String>();
		Connection conn=ConnectSql.getConn();
		String sql="SELECT `SCHEMA_NAME` FROM `information_schema`.`SCHEMATA` WHERE `SCHEMA_NAME` LIKE 'wgdata_%'";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			//	System.out.println(rs.getString(1));
			    list.add(rs.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
		ConnectSql.push(conn);
		}
		return list;
	}
	
	
	/**
	 * 獲得庫表名
	 */
	public static List<String> getFrom(String baseName){
		List<String> list=new ArrayList<String>();
		Connection conn =DBConn.getConn();
		String sql="SHOW TABLE STATUS FROM "+baseName+" WHERE ENGINE IS NOT NULL";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()){
				list.add(baseName+"."+rs.getString(1));
				//System.out.println(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				DBConn.closeConn(conn);
		}
		return list;
	}
	/**
	 * 獲得所有不包含get_date字段的表
	 */
	public static List<String> getFrom1(){
		List<String>list=new ArrayList<String>();
		Connection conn =DBConn.getConn();
		String sql="SELECT Table_schema,table_name FROM information_schema.COLUMNS WHERE column_name='get_date'";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()){
				list.add(rs.getString(1)+"."+rs.getString(2));
				//System.out.println(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				DBConn.closeConn(conn);
		}
		return list;
	}
	
	public  static Map<Integer,String> getFrom(){
		Map<Integer,String> map=new HashMap<Integer, String>();
		Connection conn =DBConn.getConn();
		String sql="SELECT DISTINCT table_name,table_id FROM `wgdata`.`data_table_list`";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()){
				map.put(rs.getInt(2), rs.getString(1));
				//System.out.println(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				DBConn.closeConn(conn);
		}
		return map;
	}
	
	
	public static void main(String[] args) {
		
	}
	
}
