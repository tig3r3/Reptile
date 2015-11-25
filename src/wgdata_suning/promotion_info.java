package wgdata_suning;


import com.syntun.putdata.InsertData;

public class promotion_info extends InsertData {
	
	public String replaceValue(String value){
		return value.replace("isPromotionPrice", "促销价格").replace("subsidies",
				"节能补贴").replace("相当于", "促销价").replace("xnItemName", "套装价")
				.replace("groupPrice", "团购价").replace("snPrice", "新团购价").replace("\"priceType\":\"4\"", "团购价")
				.replace("4", "满减").replace("8", "领卷").replace("5", "返卷").replace("7", "免运费");
	}
	
	public String editPromotion(String promotion) {
		return promotion;
	}
	
	
	public static void main(String[] args) {
		promotion_info pi = new promotion_info();
		String str = "<em>V型百叶窗式易拆型油网，拢烟面积超大，不锈钢+钢化玻璃结构材质，时尚好清洗。 </em>";
		System.out.println(pi.replaceValue(str));
	}
}
