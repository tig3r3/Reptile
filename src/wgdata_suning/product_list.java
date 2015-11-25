package wgdata_suning;

import com.syntun.putdata.InsertData;

public class product_list extends InsertData {
	
	public String pageEdit(String pageNum) {
		if (pageNum!=null || !"".equals(pageNum)) {
			return ""+Integer.parseInt(pageNum)+1;
		}
		return "0";
	}
}
