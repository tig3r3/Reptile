package com.syntun.webget;

import com.syntun.file.FileToHadoop;
import com.syntun.tools.ConnectSql;
import com.syntun.tools.SetDefaultProperty;

public class Start {
	/**
	 * 系统初始化
	 */
	public static void initSystem() {
		SetDefaultProperty.loadProperty();
	}

	/**zx
	 * 启动入口main方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 1) {
			FileToHadoop.dataStr = args[0];
		}
		// 初始化系统
		initSystem();
		System.out.println(ConnectSql.sqlHost);
		// 启动抓取地址管理
		UrlSupervise.startGeWebsite();
		// 启动解析
		ResolveWebPage runParseObj = new ResolveWebPage();
		runParseObj.startGetPage();
		// 启动效率统计计数器
//		CrawlerEnumerator.startEnumerator();
		// 程序终止处理加载
		Runtime.getRuntime().addShutdownHook(new GeterExitus());

	}
}
