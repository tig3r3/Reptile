package wgdata_898;

import com.syntun.putdata.InsertData;

public class product_comment_list extends InsertData{
	
	public String replaceScore898(String value) {
		value = value.replaceAll("<img src='../../Content/images/xing 03.JPG'>", "11").replaceAll("<img src='../../Content/images/xing 01.jpg'>", "1").replaceAll("<img src='../../Content/images/xing 02.jpg'>", "");
		value = (double)value.length()/2+"";
		
		return value;
	}
	public static void main(String[] args) {
		product_comment_list p = new product_comment_list();
		System.out.println(p.replaceScore898("<img src='../../Content/images/xing 03.JPG'><img src='../../Content/images/xing 03.JPG'><img src='../../Content/images/xing 01.jpg'><img src='../../Content/images/xing 02.jpg'>"));
		
	}
}
