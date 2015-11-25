package wgdata_dangdangwang;

import com.syntun.putdata.InsertData;

public class shop_info extends InsertData{
	public String shopinfo(String value){
		    value=value.replaceAll(" ", "").trim();
			value = value.replaceAll("<imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/>","5")
			             .replaceAll("<imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red_h.gif\"/>","4.5")
			             .replaceAll("<imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/>","4")
			             .replaceAll("<imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red_h.gif\"/>","3.5")
			             .replaceAll("<imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/>","3")
			             .replaceAll("<imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red_h.gif\"/>","2.5")
			             .replaceAll("<imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red.gif\"/>","2")
			             .replaceAll("<imgsrc=\"images/appraise_red.gif\"/><imgsrc=\"images/appraise_red_h.gif\"/>","1.5")
			             .replaceAll("<imgsrc=\"images/appraise_red.gif\"/>","1")
			             .replaceAll("<imgsrc=\"images/appraise_red_h.gif\"/>","0.5")
			             .replaceAll("<imgsrc=\"images/appraise_big_gray.gif\"/><imgsrc=\"images/appraise_big_gray.gif\"/><imgsrc=\"images/appraise_big_gray.gif\"/><imgsrc=\"images/appraise_big_gray.gif\"/><imgsrc=\"images/appraise_big_gray.gif\"/>","0")
			             
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/>","5")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small_h.gif\"/>","4.5")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/>","4")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small_h.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/>","3.5")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/>","3")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small_h.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/>","2.5")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/>","2")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small_h.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/>","1.5")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/>","1")
			             .replaceAll("<imgsrc=\"images/appraise_small_h.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/>","0.5")
			             

			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/>","5")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small_h.gif\"/>","4.5")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/>","4")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small_h.gif\"/>","3.5")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/>","3")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small_h.gif\"/>","2.5")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small.gif\"/>","2")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/><imgsrc=\"images/appraise_small_h.gif\"/>","1.5")
			             .replaceAll("<imgsrc=\"images/appraise_small.gif\"/>","1")
			             .replaceAll("<imgsrc=\"images/appraise_small_h.gif\"/>","0.5")
			             .replaceAll("<imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/><imgsrc=\"images/appraise_small_gray.gif\"/>","0");
			         
			return value;
	}
	public static void main(String[] args) {
		String s = new shop_info().shopinfo("<img src=\"images/appraise_small.gif\"/><img src=\"images/appraise_small.gif\"/><img src=\"images/appraise_small.gif\"/><img src=\"images/appraise_small.gif\"/><img src=\"images/appraise_small_gray.gif\"/> ");
		System.out.println(s+"~~~~~~~~~~~~~~~~");
	}
}