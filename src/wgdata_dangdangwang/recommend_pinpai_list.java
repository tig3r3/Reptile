package wgdata_dangdangwang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.syntun.putdata.InsertData;
import com.syntun.tools.Base64;

public class recommend_pinpai_list extends InsertData {
	
	private String reg = "[\\u0000-\\uffff]*?";
	Pattern p = Pattern.compile(reg);
	
	public  String replaceUnicode(String colValue) {
		if(colValue!=null && !colValue.equals("")) {
//			System.out.println(colValue);
			Matcher m = p.matcher(colValue);
			if(m.find()) {
				colValue = Base64.decodeUnicode(colValue.replaceAll("\\\\u(?=[0-9a-fA-F]{4})", "&#x"));
			}
//			System.out.println(colValue);
			colValue = colValue.replace("'","''");
		}
		return colValue;
	}
	
 } 


