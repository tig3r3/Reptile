package com.syntun.test;

public class IoTest {

	public static void main(String[] args) {
		// String fileName = "D:/test.txt";
		// String content = "new append!";
		// System.out.println(Math.random() * 2);
		String sql = "insert ignore into wgdata_yihaodian.product_info(";
		String s = sql.substring(sql.indexOf("insert ignore into ")
				+ "insert ignore into ".length(), sql.indexOf("("));
		System.out.println(s);
		if (s.indexOf("wgdata_suning.product_comment_list") != -1
				|| s.indexOf("wgdata_jingdong.product_comment_list") != -1
				|| s.indexOf("wgdata_tmall.product_comment_list") != -1
				|| s.indexOf("product_info") != -1
				|| s.indexOf("buy_list") != -1) {//user_info
			int nums = (int) (Math.random() * 100);
			s = s + nums;
		}
		System.out.println(s);
		// 方法一追加文件
		// AppendT

	}

}
