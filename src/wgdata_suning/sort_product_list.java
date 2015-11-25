package wgdata_suning;

import com.syntun.putdata.InsertData;

public class sort_product_list extends InsertData {
	
	public String replacePinpai(String value) {
		if (null==value || "".equals(value)) {
			return null;
		}
		return value;
	}
	
	 public static void main(String[] args) {
		sort_product_list s = new sort_product_list();
		System.out.println(s.replacePinpai("3123"));
	}
}
