package wgdata_jumei;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.syntun.putdata.InsertData;

public class product_distribute_list extends InsertData {
	public String replceType(String value) {
		value = this.UnicodeToString(value);
		if (value.contains("岁")) {
			value = "年龄分布";
		} else if (value.contains("座")||value.contains("其它")) {
			value = "星座分布";
		} else {
			value = "肤质分布";
		}
		return value;
	}

	public String replceName(String value) {
		return this.UnicodeToString(value);
	}

	public static void main(String[] args) {
		System.out.println(new product_distribute_list().replceType("20-24岁"));
	}

	private String UnicodeToString(String str) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}
}
