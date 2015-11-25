package wgdata_dangdangwang;

import com.syntun.putdata.InsertData;

public class promotion_info extends InsertData{
	public String replace(String value){
			value = value.replaceAll("<[\\S\\s]+?>","").replaceAll("promotions", "满额折");
			value =value.replaceAll("http://img39.ddimg.cn/files/36/1/11000619.swf", "满200返200").replaceAll("&yen;", "").replaceAll("&nbsp;", "").replaceAll("\\s", "");
			return value;
	}
}
