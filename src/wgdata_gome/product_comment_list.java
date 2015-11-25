package wgdata_gome;

import com.syntun.putdata.InsertData;

public class product_comment_list extends InsertData {
	
	public String replaceValue(String name) {
		      if(name.equals("100")) {
		    return "5";
	    }else if(name.equals("80")){
	    	return "4";
	    }else if(name.equals("60")){
	    	return "3";
	    }else if(name.equals("40")){
	    	return "2";
	    }else if(name.equals("20")){
	    	return "1";
	    }
		return name;
	}
  
}
