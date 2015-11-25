package wgdata_tmall;

import com.syntun.putdata.InsertData;


public class buy_list extends InsertData {
	
	public String BuyList(String value){
		return value.replaceAll("<[\\s\\S]*?>", "");
	}
	
//	private int maxCount = 5;
//	
//	private int countNum = 0;
//	
//	private int nowUrlId = 0;
//	
//	public static final long ONE_DAY = 24 * 60 * 60 * 1000l;
//	
//	@Override
//	public void setCheckInfo(CheckInfo ci) {
//		//设置查询的字段
//		ci.setSelectFiled("max(buy_time) as max_buy_time");
//		try {
//			//设置条件字段
//			ci.setWhereFiled("product_id");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	@Override
//	public CheckResult checkData (CheckValue checkValue,Map<String,String> data,UrlInfo ui) {
//		
//		CheckResult cr = new CheckResult();
//		//取出查询的字段
//		Date maxDateStr = (Date)checkValue.getObject("max_buy_time");
//		System.out.println("取出最大的时间："+maxDateStr);
//		//把该字段转为long型
//		long maxDateStrLong = maxDateStr.getTime();
//		System.out.println("取出最大时间Long型："+maxDateStrLong);
//		
//		//取出数据中的需要对比的字段
//		String dataDateStr = data.get("buy_time");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		try {
//			//格式化成日期
//			Date dataDate = sdf.parse(dataDateStr);
//			System.out.println("页面取出时间："+dataDate);
//			//用取出的时间long型减去18天
//			Date resultTime = new Date(maxDateStrLong-ONE_DAY * 18);
//			System.out.println("结果时间："+resultTime);
////			System.exit(0);
//			//比较，如果数据中的字段在上批抓取最大值之前，则需要停止访问其平级或下级页面
//			if(dataDate.before(resultTime)) {
//				if(ui.getGetUrlId()!=nowUrlId) {
//					countNum = 0;
//					this.nowUrlId = ui.getGetUrlId();
//					System.out.println("比较了第一次！");
//				}
//				countNum++;
//				if(countNum>maxCount) cr.setStopSubLevel(ui);	//根据抓取具体情况判断是停止下一级地址
//				else return null;
//			}
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			System.out.println("eeeeeeeeeeeeeeeeeee");
//			return null;
//		}
//		return cr;
//		
//	}
	
//	public static void main(String[] args) {
//		String max = "2012-09-12 12:12:12";
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		try {
//			//格式化成日期
//			Long jianshu = sdf.parse(max).getTime();
////			
//			System.out.println(jianshu);
//			long currentTime =  System.currentTimeMillis();
//			if (jianshu < (currentTime - ONE_DAY * 18)) {
//				System.out.println("退出！！");
//			}		
//
//		
//	} catch (ParseException e) {
//		
//		System.out.println(e.getMessage());
//	}
//	
//  }
}

