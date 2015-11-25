package wgdata_yihaodian;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.syntun.putdata.InsertData;

public class product_comment_list extends InsertData{
	
	public String replaceUserId(String value){
		return value.replaceAll("\\\\t", "").replaceAll("\\\\r", "").replaceAll("\\\\n", "").trim();
	}
	
	public String replaceContent(String value){
		Pattern p = Pattern.compile("<img src=\"http://image.yihaodianimg.com/try/member/images/star_16x16_on.png\"/>");  
		Matcher m = p.matcher(value);  
		int i = 0;
		while (m.find()) {  
		    i++;
		} 
		if(i != 0)	
			value = String.valueOf(i);
		String str = "";
		str = value.replace("优秀评价奖励50积分", ".");
		return str.replaceAll("\\s*<[\\s\\S]+?>\\s*", "").replace("&nbsp;", " ").replaceAll("\\\\t", "").replaceAll("\\\\r", "").replaceAll("\\\\n", "").trim();
	}
}
