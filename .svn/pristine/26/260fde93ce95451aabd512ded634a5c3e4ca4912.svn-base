package wgdata_qq;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.syntun.putdata.InsertData;

public class buy_list extends InsertData {

	public String replaceTime(String time) {
		Long s = Long.parseLong(time + "000");
		//2014-05-29 16:11:06
		Date date = new Date(s);
		SimpleDateFormat aa = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return aa.format(date);
	}
	
}
