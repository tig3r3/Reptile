package wgdata_jingdong;

import com.syntun.putdata.InsertData;

public class promotion_info extends InsertData {

	public static void main(String[] args) {

		System.out.println(new InsertData().replaceUnicode("UOFgGMC8SOFNT"));
	}

	public String replaceTypeName(String value) {
		String value_str = new InsertData().replaceUnicode(value);
		return value_str.replace("subsidyMoney", "节能补贴")
				.replace("fullLadderDiscountList", "满减")
				.replace("needMondey", "满赠")
				.replace("haveFullRefundGifts", "加价购").replace("price", "会员特享")
				.replace("adwordGiftSkuList", "赠品").replace("discount", "已优惠")
				.replace("couponQouta", "赠劵").replace("minNum", "多买优惠")
				.replace("score", "赠积分").replace("\"extType\":4", "团购")
				.replace("\"extType\":8", "闪团").replace("percent", "满减")
				.replace("needJBeanNum", "京豆优惠购");
	}

	public String replaceTypeInfo(String value) {
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
				.replaceAll("cent", "%").replace("needJBeanNum", "京豆优惠购");
	}

}
