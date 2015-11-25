package wgdata_yixun;

import com.syntun.putdata.InsertData;

public class promotion_info extends InsertData {
	public String replacePromotion_type_info(String value) {
		value = value.replaceAll("&yen;", ":");
		if(value.equals("")){
			value="NULL";
		}
		return value;
	}

	public static void main(String[] args) {
		String s = new promotion_info().replaceName("\"discount_type\":\"5\"");
		System.out.println(s);
	}

	public String replaceName(String value) {
		value = value.replaceAll("狂降|直降|降", "降");
		value = value.replaceAll("\"name\"", "赠品").replaceAll("\"discount_type\":\"5\"", "满立减").replaceAll("\"discount_type\":\"8\"", "满换购")
				.replace("php_promo_rule_", "促销");
		// System.out.println(value);
		return value;

	}

}
