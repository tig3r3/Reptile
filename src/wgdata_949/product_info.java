package wgdata_949;

import com.syntun.putdata.InsertData;

public class product_info extends InsertData{
	
	public String replaceValue949(String value) {
		value = value.replaceAll("<[^>]*>", "");
		
		return value;
	}
	public static void main(String[] args) {
		product_info p = new product_info();
		System.out.println(p.replaceValue949("<font color=\"#000000\">汉语拼音</font>"));
		
	}
}
