package com.syntun.putdata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syntun.tools.ConnectSql;


public class InspectInformation {
	/**
	 * 表名称
	 */
	private String tableName;
	/**
	 * where字段
	 */
	private List<String> whereFileds = new ArrayList<String>();
	/**
	 * select字段
	 */
	private List<String> selectFileds = new ArrayList<String>();
	/**
	 * 表的字段类型
	 */
	private Map<String,Integer> tableFiledType = new HashMap<String,Integer>();
	/**
	 * 已查询的InspectValue
	 */
	private Map<String,InspectValue> saveInspectValue = new HashMap<String,InspectValue>();
	/**
	 * 构造函数
	 * @param tableName
	 */
	public InspectInformation(String tableName) {
		this.tableName = tableName;
		Connection conn = ConnectSql.getConn();
		try {
			DatabaseMetaData dmd = conn.getMetaData();
			String dataBaseName = null;
			if(tableName.indexOf(".")!=-1){
				String[] tableInfo = tableName.split("\\.");
				dataBaseName = tableInfo[0];
			}
			ResultSet rs = dmd.getColumns(dataBaseName,null,tableName,null);
			while(rs.next()) {
				tableFiledType.put(rs.getString("COLUMN_NAME"), rs.getInt("DATA_TYPE"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("获取表信息失败");
			System.exit(0);
			e.printStackTrace();
		}
		ConnectSql.push(conn);
	}
	/**
	 * 设置where字段
	 * @param filedName
	 * @throws Exception 
	 * 
	 */
	public void setWhereFiled(String filedName) throws Exception {
		if(this.tableFiledType.containsKey(filedName))
			this.whereFileds.add(filedName);
		else
			throw new Exception("表中不存在该字段！");
	}
	/**
	 * 设置select字段
	 * @param filedName
	 * @throws Exception 
	 */
	public void setSelectFiled(String filedName) {
		this.selectFileds.add(filedName);
	}
	/**
	 * 进行查询
	 * @throws SQLException 
	 * @throws ParseException 
	 */
	public List<InspectValue> exeSelect(List<HashMap<String, String>> dataList) {
		
		List<InspectValue> inspectValueList =  new ArrayList<InspectValue>();
		try{
			if(this.whereFileds.size()>0 && selectFileds.size()>0) {
				//生成select语句
				String sql = "select ";
				for(int index=0;index<selectFileds.size();index++) {
					if(index!=0) sql += ",";
					sql += selectFileds.get(index);
				}
				//生成where语句
				sql += " from "+this.tableName+" where ";
				for(int index=0;index<whereFileds.size();index++) {
					if(index!=0) sql += " and ";
					sql += whereFileds.get(index)+"=?";
				}
				//带入数据查询
				Connection conn = ConnectSql.getConn();
				for(int i=0;i<dataList.size();i++) {
					HashMap<String,String> data = dataList.get(i);
					PreparedStatement ps = conn.prepareStatement(sql);
					String valueKey = "_";
					for(int j=1;j<=whereFileds.size();j++) {
						String value = data.get(whereFileds.get(j-1)).trim();
						valueKey += value+"_";
						ps.setObject(j, value,this.tableFiledType.get(whereFileds.get(j-1)));
					}
					//取出或查询cv
					if(this.saveInspectValue.containsKey(valueKey)) {
						InspectValue cv = this.saveInspectValue.get(valueKey);
						if(cv!=null) inspectValueList.add(cv);
					}
					else {
						InspectValue cv = new InspectValue(i);
						ResultSet rs = ps.executeQuery();
						ResultSetMetaData rsd =  rs.getMetaData();
						int len = rsd.getColumnCount();
						if(rs.next()) {
							boolean isNull = true;
							for(int g=1;g<=len;g++){
								if(rs.getObject(g)!=null) isNull = false;
								cv.setObject(rsd.getColumnName(g), rs.getObject(g));
							}
							if(!isNull){
								inspectValueList.add(cv);
								this.saveInspectValue.put(valueKey,cv);
							}
							else this.saveInspectValue.put(valueKey,null);
						}
					}
					ps.close();
				}
				ConnectSql.push(conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据检查失败！！！");
			System.exit(0);
		}
		return inspectValueList;
	}
	
	public static void main(String [] args) {
		InspectInformation ci = new InspectInformation("wgdata_360buy.product_comment_list");
		try {
			ci.setSelectFiled("max(product_comment_time)");
			ci.setWhereFiled("product_id");
			
			
			
			ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>();
			HashMap<String,String> hm1 = new HashMap<String,String>();
			hm1.put("product_id","633997");
			HashMap<String,String> hm2 = new HashMap<String,String>();
			hm2.put("product_id","633998");
			HashMap<String,String> hm3 = new HashMap<String,String>();
			hm3.put("product_id", "0");
			HashMap<String,String> hm4 = new HashMap<String,String>();
			hm4.put("product_id","633998");
			al.add(hm1);
			al.add(hm2);
			al.add(hm3);
			al.add(hm4);
			System.out.println(ci.exeSelect(al));
			System.out.println(ci.exeSelect(al));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
