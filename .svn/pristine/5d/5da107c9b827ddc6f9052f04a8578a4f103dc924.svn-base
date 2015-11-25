package com.syntun.tools;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GetAllClass {
	/**
	 * 需要遍历的包列表
	 */
	private static List<String> packageList = new ArrayList<String>();
	/**
	 * 初始化
	 */
	static {
		packageList.add("com.syntun.tools");
		packageList.add("com.syntun.tools.filetodb");
		packageList.add("com.syntun.putdata");
		packageList.add("com.syntun.replace");
		packageList.add("com.syntun.webget");
	}
	
	/**
	 * 获取所有类
	 */
	public static List<Class<?>> getAllClassList() {
		List<Class<?>> classList= new LinkedList<Class<?>>();
		try {
			for( int i=0;i<packageList.size();i++ ) {
				List<Class<?>> packageClassList = PackageUtil.getClasses(packageList.get(i));
				classList.addAll(packageClassList);		
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("IO访问类失败");
			System.exit(0);
		}
		return classList;
	}
}
