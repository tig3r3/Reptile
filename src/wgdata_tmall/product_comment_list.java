package wgdata_tmall;

import com.syntun.putdata.InsertData;


public class product_comment_list extends InsertData {
	
//	private int maxCount = 5;
//	
//	private int countNum = 0;
//	
//	private int nowUrlId = 0;
//	
//	@Override
//	public void setCheckInfo(CheckInfo ci) {
//		//设置查询的字段
//		ci.setSelectFiled("max(prodct_comment_time) as max_comment_time");
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
//		Date maxDateStr = (Date)checkValue.getObject("max_comment_time");
//		//取出数据中的需要对比的字段
//		String dataDateStr = data.get("prodct_comment_time");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			//格式化成日期
//			Date dataDate = sdf.parse(dataDateStr);
//			//比较，如果数据中的字段在上批抓取最大值之前，则需要停止访问其平级或下级页面
//			if(dataDate.before(maxDateStr)) {
//				if(ui.getGetUrlId()!=nowUrlId) {
//					countNum = 0;
//					this.nowUrlId = ui.getGetUrlId();
//				}
//				countNum++;
//				if(countNum>maxCount) {
//					cr.setStopFlatLevel(ui);	//根据抓取具体情况判断是停止平级地址
//					System.out.println("平级平级平级平级平级平级平级平级");
//					}
//				else return null;
//			}
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			return null;
//		}
//		
//		return cr;
//		
//	}
}

