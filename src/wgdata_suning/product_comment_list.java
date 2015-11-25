package wgdata_suning;

import com.syntun.putdata.InsertData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class product_comment_list extends InsertData {
	
	private String regex = "(star_hight)";
	Pattern p = Pattern.compile(regex);
	
	private String reg = "\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}\\.\\d+";
	Pattern p2 = Pattern.compile(reg);
	
	public String count(String value) {
		if (value.contains("star_hight")){
			int i = 0;
			Matcher m = p.matcher(value);
			while (m.find()) {
				i++;
			}
//			System.out.println(i+"分");	
			return ""+i;
		}
		return value;
	}	
	
	public String formatTime(String value) {
		Matcher m = p2.matcher(value);
		if (m.find()) return value.replaceAll("\\.\\d+", "");
		return value;
	}
	public String replaceValue(String value){
		if(value.equals("69px")){
			return "5";
		}else if(value.equals("55px")){
			return "4";
		}else if(value.equals("41px")){
			return "3";
		}else if(value.equals("28px")){
			return "2";
		}else if(value.equals("14px")){
			return "1";
		}
		 return value;
	}
	public static void main(String[] args) {
		 product_comment_list pcl = new product_comment_list();
		//System.out.println(pcl.formatTime("2012-08-24 11:49:42.699"));
		System.out.println(pcl.replaceValue("28px"));
	}
	
}
