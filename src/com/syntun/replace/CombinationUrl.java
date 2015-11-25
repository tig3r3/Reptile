package com.syntun.replace;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import com.syntun.tools.ConnectSql;

public class CombinationUrl {
	/**
	 * 拼接模板集合
	 */
	private static HashMap<Integer,HashMap<Integer,HashMap<String,String>>> cPurlHs = new HashMap<Integer,HashMap<Integer,HashMap<String,String>>>();
	/**
	 * 获取拼接模版数据
	 * 
	 * @param patternUrlId
	 * @return
	 */
	@SuppressWarnings("unused")
	private HashMap<Integer,HashMap<String,String>> getPurlInfo(int patternUrlId){
		
		HashMap<Integer,HashMap<String,String>> combinationUrlHs = null;
		if(cPurlHs.containsKey(patternUrlId)) {
			synchronized (cPurlHs) {
				combinationUrlHs = cPurlHs.get(patternUrlId);
			}
		}
		else{
			Connection conn = ConnectSql.getConn();
			try {
				combinationUrlHs = new HashMap<Integer,HashMap<String,String>>();
				Statement stmt = conn.createStatement();
				String sql = "select * from purl_list where url_group=" + patternUrlId;
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()) {
					HashMap<String,String> info = new HashMap<String,String>();
					info.put("exe_params", rs.getString("exe_params"));
					info.put("url_group", rs.getString("url_group"));
					info.put("id", rs.getString("id"));
					info.put("sort_id", rs.getString("sort_id"));
					info.put("generate_sort_id", rs.getString("generate_sort_id"));
					info.put("purl_str",rs.getString("purl_str"));
					combinationUrlHs.put(rs.getInt("id"),info);
				}
				synchronized (cPurlHs) {
					cPurlHs.put(patternUrlId, combinationUrlHs);
				}
				rs.close();
				stmt.close();
				ConnectSql.push(conn);
				return combinationUrlHs;
				
			} catch(Exception sqle) {
				ConnectSql.push(conn);
				sqle.printStackTrace();
				System.out.println("加载拼接模板失败！！");
				System.exit(0);
			}
		}
		return combinationUrlHs;
	}
	/**
	 * 清空拼接集合
	 */
	public static void clear() {
		cPurlHs.clear();
	}
	/**
	 * 查看当前地址对象是否可以组合地址
	 * 
	 * @param urlInfo
	 */
//	public void getCombinationUrl(Url urlInfo) {
//		
//		HashMap<Integer,HashMap<String,String>> combinationUrlHs = this.getPurlInfo(urlInfo.getPatternUrlId());
//		for(Iterator<Entry<Integer, HashMap<String,String>>> iter = combinationUrlHs.entrySet().iterator();iter.hasNext();){
//			Entry<Integer, HashMap<String,String>> e = iter.next();
//			HashMap<String,String> pInfo = e.getValue();
//			String generateSortId = pInfo.get("generate_sort_id");
//			String pUrlStr = pInfo.get("purl_str");
//			int patternUrlId = Integer.parseInt(pInfo.get("url_group"));
//			String sortId = ","+pInfo.get("sort_id")+",";
//			int urlSortId = urlInfo.getSortId();
//			int sortIdIndex = sortId.indexOf(","+urlSortId+",");
//			boolean isFind = false;
//			if(urlInfo.getPatternUrlId()==patternUrlId){
//				if(sortIdIndex!=-1 && urlSortId!=0) isFind = true;
//			}
//			if(!isFind) continue;
//			LinkedList<Url> urlList;
//			try {
//				urlList = ReplaceExecParameter.execParameter(pUrlStr,urlInfo, pInfo.get("exe_params"),generateSortId);
//			} catch (Exception ex){
//				ex.printStackTrace();
//				urlList = null;
//			}
//			
//			if(urlList!=null && urlList.size()>0) {
//				for(int i=0;i<urlList.size();i++) {
//					UrlSupervise.addUrlInfo(urlList.get(i));
//				}
//			}
//		}
//	}
}
