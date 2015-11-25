package wgdata_qq;

import com.syntun.putdata.InsertData;

public class promotion_info extends InsertData {

	public String replace(String str){
		return str.replace("<em>", "").replace("</em>", "").replace("desc", "满减").replace("</a>", "");
	}
	public String replaceTypeInfo(String str){
		str=str+" ";
		return str.replaceAll("<[\\s\\S]+?>", " ")
				.replaceAll("<.+?\\s", " ")
				.replaceAll("&nbsp;", " ")
				.replaceAll("\\\\t", "")
				.replaceAll("\\\\n", "")
				.replaceAll("\\\\r", "")
				.trim();
	}
	public static void main(String[] args) {
		String str = "满3件就打9.5折，送<ahref='http:\\/\\/item.wanggou.com\\/FF1DDC0A00000000040100003193C97F'target='_blank'>一分四USB分线器，加35.00元可换购<ahref='http:\\/\\/item.wanggou.com\\/FF1DDC0A000000000401";
		String s = new promotion_info().replaceTypeInfo(str);
		System.out.println(s);
	}
}
