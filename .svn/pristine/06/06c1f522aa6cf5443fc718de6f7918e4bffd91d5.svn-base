package wgdata_yihaodian;

import org.apache.commons.lang.StringEscapeUtils;

import com.syntun.putdata.InsertData;

public class product_info extends InsertData {

	public String replaceInfoValue(String value){
		String str =  value.replaceAll("<.+?>", "").replace("&nbsp;", " ");
		 return StringEscapeUtils.unescapeHtml(str);
	}
	public String replaceInfoFiled(String value){
		return value.replace("&nbsp;", " ");
	}
	
//	public static void main(String[] args) {
//		String s = "&nbsp;";
//		String str = StringEscapeUtils.unescapeHtml(s);
//		System.out.println(str);
//	}
}


