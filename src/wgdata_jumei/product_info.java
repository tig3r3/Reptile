package wgdata_jumei;

import com.syntun.putdata.InsertData;

public class product_info extends InsertData{
	public String replceName(String value){
		value=value.replaceAll("&nbsp;", "");
		return value;
	}
}
