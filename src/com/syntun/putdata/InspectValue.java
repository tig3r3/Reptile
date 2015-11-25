package com.syntun.putdata;

import java.util.Date;
import java.util.HashMap;

/**
 * 存储需要检查的数据
 * 
 */
public class InspectValue {

	private int id;
	/**
	 * 存储比较值
	 */
	private HashMap<String,Object> valueHs = new HashMap<String,Object>();
	/**
	 * 存储字段类型
	 * 
	 * 1-Integer
	 * 2-String
	 * 3-Date
	 * 4-Double
	 * 5-Object
	 */
	private HashMap<String,Integer> valueTypeHs = new  HashMap<String,Integer>();
	
	public InspectValue(int id) {
		this.id = id;
	}
	public void setInt(String filedName,int value) {
		valueHs.put(filedName,new Integer(value));
		valueTypeHs.put(filedName,1);
	}
	
	public void setString(String filedName,String value) {
		valueHs.put(filedName, value);
		valueTypeHs.put(filedName,2);
	}
	
	public void setDate(String filedName,Date value) {
		valueHs.put(filedName, value);
		valueTypeHs.put(filedName,3);
	}
	
	public void setDouble(String filedName,Double value) {
		valueHs.put(filedName, value);
		valueTypeHs.put(filedName,4);
	}
	
	public void setObject(String filedName,Object value) {
		valueHs.put(filedName, value);
		valueTypeHs.put(filedName,5);
	}
	
	public int getInt (String filedName) throws Exception {
		if(valueTypeHs.get(filedName)==1) {
			return (Integer)valueHs.get(filedName);
		}
		else throw new Exception("类型异常");
	}
	
	public String getString (String filedName) throws Exception {
		if(valueTypeHs.get(filedName)==2) {
			return (String) valueHs.get(filedName);
		}
		else throw new Exception("类型异常");
	}
	
	public Date getDate(String filedName) throws Exception {
		if(valueTypeHs.get(filedName)==3) {
			return (Date) valueHs.get(filedName);
		}
		else throw new Exception("类型异常");
	}
	
	public Double getDouble(String filedName) throws Exception {
		if(valueTypeHs.get(filedName)==3) {
			return (Double) valueHs.get(filedName);
		}
		else throw new Exception("类型异常");
	}
	
	public Object getObject(String filedName) {
		return valueHs.get(filedName);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
