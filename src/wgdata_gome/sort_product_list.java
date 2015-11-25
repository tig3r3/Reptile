package wgdata_gome;

import com.syntun.putdata.InsertData;

public class sort_product_list extends InsertData {
	
	public String replaceName(String name) {
		if((int)name.charAt(name.length()-1)==160) {
			return name.substring(0,name.length()-2);
		}
		return name;
	}
	
}
