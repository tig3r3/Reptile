package wgdata_jumei;

import com.syntun.putdata.InsertData;

public class product_comment extends InsertData {
		public String replceTime(String value){
//			//设置日期格式
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			// new Date()为获取当前系统时间
//			Date date=new Date();
//			long time=Long.parseLong(value)*(1000*60*60*24);
//			long oldtime=date.getTime()-time; 
//			Date dt = new Date(oldtime);
//			value=df.format(dt);
			return value;
		}
		public String replceScore(String value){
			value=(Integer.parseInt(value))/16+"";
			return value;
		}
		
		public String replceContent(String value){
			value=value.replaceAll("<.*?>", "").replaceAll("&nbsp;","")
					.replaceAll("\\s*","")
					.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			return value;
		}
		
		
		public static void main(String[] args) {
			System.out.println(new product_comment().replceScore("64"));

		}
}
