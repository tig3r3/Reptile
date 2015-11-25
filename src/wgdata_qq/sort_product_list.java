package wgdata_qq;

import com.syntun.putdata.InsertData;

public class sort_product_list extends InsertData {

	public String replace(String str){
		
		
		return str.replace("<em>", "").replace("</em>", "");
	}
	
}
