package com.syntun.webget;

import com.syntun.putdata.InsertData;

public class GeterExitus extends Thread {

	public void run() {
		System.out.println("system run close...");
		InsertData.clearInsertData(false);
		UrlSupervise.clearUrlInfos();
		ResolveWebPage.resetParse();
		synchronized (ContentManager.list) {
			ContentManager.excuteWriteFile(ContentManager.list);
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~insert content 。。。。。。。。。。");
		}
		System.out.println("system run closeed");
	}
}
