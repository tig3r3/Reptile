package wgdata_dangdangwang;

import com.syntun.putdata.InsertData;

public class product_info extends InsertData{
	public String replace(String value){
			value = value.replaceAll("<[\\s\\S]+?>", "");
			return value;
		}
//	public static void main(String[] args) {
//		String s = new product_info().replace("</td></tr><tr><td></td></tr><tr><td>电源功率（w）---179W");
//		System.out.println(s);
//	}
}
