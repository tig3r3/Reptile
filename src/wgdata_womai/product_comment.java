package wgdata_womai;

import com.syntun.putdata.InsertData;

public class product_comment extends InsertData {
	
	public String replaceScore(String value){
		return value.replaceAll("/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg", "5").trim()
				.replaceAll("/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg", "4")
				.replaceAll("/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg", "3")
				.replaceAll("/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg", "2")
				.replaceAll("/zhongliang/templets/green2014/images/detail/star_on.jpg", "1");

	}
	public static void main(String[] args) {
		String s="/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg/zhongliang/templets/green2014/images/detail/star_on.jpg";
		System.out.println(new product_comment().replaceScore(s));
	}
}
