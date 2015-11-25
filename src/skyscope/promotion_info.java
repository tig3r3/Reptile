package skyscope;

import com.syntun.putdata.InsertData;

public class promotion_info extends InsertData {

	public String replaceTypeName(String value) {
		String value_str = new InsertData().replaceUnicode(value);
		return value_str.replace("subsidyMoney", "节能补贴")
				.replace("fullLadderDiscountList", "满减")
				.replace("needMondey", "满赠")
				.replace("haveFullRefundGifts", "加价购").replace("price", "会员特享")
				.replace("adwordGiftSkuList", "赠品").replace("discount", "已优惠")
				.replace("couponQouta", "赠劵").replace("minNum", "多买优惠")
				.replace("score", "赠积分").replace("\"extType\":4", "团购")
				.replace("\"extType\":8", "闪团")
				.replace("needJBeanNum", "京豆优惠购").replace("percent", "满减");
	}

	public String nullToKong(String value) {
		String value_str = new InsertData().replaceUnicode(value);
		if (value_str.indexOf("null") == (value_str.length() - 4)) {
			value_str = value_str.replace("null", "");
		}

		if (value_str.indexOf("京东自营") == (value_str.length() - 4)) {
			value_str = "京东自营";
		}
		return value_str;
	}

	public static void main(String[] args) {
		String str = "sdf京东自营";
		System.out.println(str.toLowerCase().indexOf("null"));
		if (str.toLowerCase().indexOf("京东自营") == (str.length() - 4)) {
			System.out.println("~~~");
		}
	}

	public static String replaceTypeInfo(String value) {
		String value_str = new InsertData().replaceUnicode(value);
		return value_str.replaceAll("\"needMoney\":", "满")
				.replaceAll(",\"needMondey", "每满")
				.replaceAll("needMondey", "满")
				.replaceAll("\"rewardMoney\":", "减")
				.replaceAll(",\"reward", "可减现金")
				.replaceAll("\"reward\":", "立减")
				.replaceAll("limitTimePromo", "元即赠热销商品，赠完即止")
				.replaceAll("\"topMoney\":\"0.0\"", "")
				.replaceAll("topMoney", "最多可减")
				.replaceAll("addMoney", "即可购买热销商品   需另加")
				.replaceAll("price", "铁牌用户及以上会员价:")
				.replaceAll(",\"number", "x").replaceAll("discount", "已优惠")
				.replaceAll("couponQouta", "赠送京劵￥").replaceAll("minNum", "购买")
				.replaceAll("maxNum", "件及以上，￥")
				.replaceAll("fullRefundType", "为您节省￥")
				.replaceAll("limitUserType", "，购买超过")
				.replaceAll("score", "件时不享受该优惠")
				.replaceAll(",\"minPoolNum", "购买至少")
				.replaceAll("PoolNum", "类商品，").replaceAll("\"", "")
				.replaceAll(":", "").replaceAll("per", "元，可减")
				.replaceAll("needJBeanNum", "京豆优惠购").replaceAll("cent", "%");
	}

	public String replaceName(String value) {
		value = value.replaceAll("狂降|直降|降", "降");
		value = value.replaceAll("name", "返劵").replace("desc", "优惠信息")
				.replace("php_promo_rule_", "促销");
		// System.out.println(value);
		value = value.replaceAll("&yen;", ":");
		return value;

	}

	public String replace(String value) {
		value = value.replaceAll("<[\\S\\s]+?>", "").replaceAll("promotions",
				"满额折");
		value = value
				.replaceAll("http://img39.ddimg.cn/files/36/1/11000619.swf",
						"满200返200").replaceAll("&yen;", "")
				.replaceAll("&nbsp;", "").replaceAll("\\s", "");
		value = value.replace("<em>", "").replace("</em>", "")
				.replace("desc", "满减").replace("</a>", "");
		return value;
	}

	public String replaceValue(String value) {
		return value.replace("送&nbsp;&nbsp;积&nbsp;&nbsp;分", "送积分")
				.replace("discount", "优惠").replace("gift", "赠品")
				.replace("blue", "返券").replace("scheme\":1", "满减")
				.replace("scheme\":2", "满返").replace("rebate", "打折")
				.replace("\"type\":\"直降", "已优惠")
				.replace("isPromotionPrice", "促销价格")
				.replace("subsidies", "节能补贴").replace("相当于", "促销价")
				.replace("xnItemName", "套装价").replace("groupPrice", "团购价")
				.replace("snPrice", "新团购价");
	}

	public String replaceSuNingName(String value) {
		return value.replace("4", "满减").replace("5", "反劵").replace("8", "领劵");
	}

}