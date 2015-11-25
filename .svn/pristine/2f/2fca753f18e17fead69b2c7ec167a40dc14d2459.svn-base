package wgdata_yixun;

import com.syntun.putdata.InsertData;

import java.text.SimpleDateFormat;
import java.util.Date;



public class product_comment_list extends InsertData{

	public String getStringDate(String longTime){
		if (longTime.length()==10 && longTime.matches("\\d{10}")) {
			longTime = longTime + "000";
			long lSysTime = Long.parseLong(longTime); 	//时间字符串转long类型
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dt = new Date(lSysTime); 
			String sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
			return sDateTime;
		}
		return longTime;
	}
	
		public String replacValue(String value){
			if(value.equals("0")){
				try{ 
					value = value.replace("0", "NULL");
					System.out.println(value);
			}catch(Exception e){
				e.printStackTrace();
			}
			}
			return value;
		}
	
	
//public static void main(String[] args) {
//	System.out.println(new product_comment_list().replaceValue("0"));
//	new product_comment_list().getStringDate("1346052730");
// new product_comment_list().replacValue("0");

//}
}


