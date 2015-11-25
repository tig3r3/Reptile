package wgdata_888;

import com.syntun.putdata.InsertData;

public class product_comment_list extends InsertData{
	
	public String replaceScore888(String value) {
		value = value.replaceAll("<span></span>", "1").replaceAll("<span", "");
		value = value.length()+"";
		
		return value;
	}
	public static void main(String[] args) {
		product_comment_list p = new product_comment_list();
		System.out.println(p.replaceScore888("<span></span><span></span><span"));
		
	}
}
