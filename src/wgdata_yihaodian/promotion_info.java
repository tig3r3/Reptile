package wgdata_yihaodian;

import com.syntun.putdata.InsertData;

public class promotion_info extends InsertData {
	public String replaceTypeInfo(String value) {
		return value.replaceAll("<[\\s\\S]+?>", "").replaceAll("&nbsp;", " ")
				.replaceAll("\\\\t", "").replaceAll("\\\\n", "")
				.replaceAll("\"promoteType\":4", "团购价").replaceAll("\\\\r", "").trim();
	}

	public String replaceAboutInfo(String value) {
		return value.replaceAll("<[\\s\\S]+?>", " ").replaceAll("\\\\t", "")
				.replaceAll("\\\\n", "").replaceAll("\\\\r", "").trim();
	}

	public static void main(String[] args) {
		String str = "本商品单笔满<b>5<\\/b>件及以上，前<b>5<\\/b>件<b>9.7<\\/b>折";
		String s = new promotion_info().replaceTypeInfo(str);
		System.out.println(s);
	}
}