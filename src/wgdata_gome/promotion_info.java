package wgdata_gome;

import com.syntun.putdata.InsertData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class promotion_info extends InsertData {

	private String reg = "优惠\\s*(.+?)\\d";
	Pattern p = Pattern.compile(reg);
	private String reg2 = "节能补贴(.+?)\\d+.{0,1}\\d*[元]?";
	Pattern p2 = Pattern.compile(reg2);
	private String reg3 =  "([\u4e00-\u9fa5]*节能补贴[￥]?\\d+[元]?[，|。|！|；])";
	Pattern p3 = Pattern.compile(reg3);
	
	public String replaceValue(String value) {
		return value.replace("送&nbsp;&nbsp;积&nbsp;&nbsp;分", "送积分").replace(
				"discount", "优惠").replace("gift", "赠品").replace("blue", "返券")
				.replace("scheme\":1", "满减").replace("scheme\":2", "满返").replace("rebate", "打折").replace("\"type\":\"直降", "已优惠").replace("\"type\":\"ZHIJIANG\"","已优惠");
	}
	
	public String replaceInfo(String value) {
		Matcher m = p.matcher(value);
		if (m.find()) {
			String errStr = m.group(1);
			return value.replace(errStr, "￥");
		}
		Matcher m3 = p3.matcher(value);
		while (m3.find()) {
			String JNStr = m3.group(1);
			return value.replace(JNStr, "");
		}
		Matcher m2 = p2.matcher(value);
		if (m2.find()) {
			String errStr = m2.group(1);
			return value.replace(errStr, "￥");
		}
		return value;
	}
	public String replaceTypeInfo(String value){
		String s=new promotion_info().replaceInfo(value);
		s=s+" ";
		s= s.replaceAll("(&lt;|&lt)[\\s\\S]+?(&gt;|&gt)", " ")
				.replaceAll("&lt.+?\\s", " ")
				.replaceAll("(&amp;nbsp;|&ampnbsp)", " ")
				.replaceAll("&nbsp;", " ")
				.replaceAll("\\\\t", "")
				.replaceAll("\\\\n", "")
				.replaceAll("\\\\r", "")
				.trim();
				return s;
	}
	public static void main(String[] args) {
		String str = "&lt;p&gt;&lt;spanstyle=&#034;font-family:&#039;MicrosoftYaHei&#039;;font-size:16px;color:#cc0000;line-height:20px;-webkit-text-size-adjust:none;&#034;&gt;4.5英寸四核1.2G&amp;nbsp;&amp;nbsp;运行内存1GB双卡双待支持联通3G移动2G神机莫过于此！！！&lt;/span&gt;&lt;/p&gt;&lt;p&gt;&lt;span";
		String s = new promotion_info().replaceTypeInfo(str);
		System.out.println(s);
	}
}
