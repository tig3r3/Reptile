package com.syntun.replace;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.syntun.tools.ConnectSql;
import com.syntun.tools.PurlFiledList;
import com.syntun.tools.PurlFun;
import com.syntun.webget.ResolveWebPage;
import com.syntun.webget.Url;

/**
 * 
 * 对参数进行替换
 * 
 * {D--dBName.tableName.fieldName--} 从数据库提取 {S--sql--}sql语句,单一结果集 {J--|a/b|--}
 * 需要执行的Javascript代码，或数学表达式(***未实现***) {P--min-max--}最大值到最小值生成 数据列
 * {U2--xxxxxx--}两次urlencode {U--xxxxxx--}url编码 {--xxxxxxxxxx--} 参数替换
 * 
 * 如果其中带有[xxxxx]表球用UrlInfo对像中携带的数据进行替换,如：{S--select xxx from xxx where
 * sortname='[sortName]'}
 * 
 */
public class ReplacereplaceParameter extends PurlFun {
	// 已拼接列表
	// private static LinkedHashSet<String> pEdList = new
	// LinkedHashSet<String>();
	//
	// private static final int P_ED_LIST_SIZE = 40;

	private static HashMap<String, LinkedList<ParameInfo>> paramHm = new HashMap<String, LinkedList<ParameInfo>>();

	public static List<PurlFiledList> listPurlFiled = new ArrayList<PurlFiledList>();

	public static void getPurlFiledList() {
		// 查询表名称
		String sql = "SELECT `url_group`, `generate_sort_id`, `field_str`, `filed_fun` FROM purl_filed_list";
		Connection conn = ConnectSql.getConn();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				// System.out.println(rs.getString("filed_fun"));
				PurlFiledList p = new PurlFiledList();
				p.setFieldStr(rs.getString("field_str"));
				p.setFiledFun(rs.getString("filed_fun"));
				p.setGenerateSortId(rs.getString("generate_sort_id"));
				p.setUrlGroup(rs.getString("url_group"));
				listPurlFiled.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
	}

	/**
	 * 对连接进替换
	 * 
	 * @param pUrl
	 * @param ui
	 * @param exeParams
	 * @param pUrlId
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public synchronized static LinkedList<Url> execParameter(String pUrl,
			Url ui, String exeParams, String generateSortId)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String[] exeParamArr = exeParams.split(",");
		LinkedList<Url> uiList = null;
		HashMap<String, LinkedList<ParameInfo>> hm = getParamValues(ui,
				exeParams);
		if (hm != null)
			uiList = parallelReplace(pUrl, hm, exeParamArr, ui, generateSortId);
		return uiList;

	}

	// /**
	// *
	// * 搜索已拼接完成的列表，列表中只保存固定数量的Key
	// *
	// * @param pEdKey
	// * @return
	// */
	// private static boolean searchPEd(HashMap<String,LinkedList<ParameInfo>>
	// paramHm,int pUrlId) {
	// //生成key,
	// String pEdKey = "p="+pUrlId;
	// for(Iterator<Entry<String, LinkedList<ParameInfo>>> i =
	// paramHm.entrySet().iterator();i.hasNext();) {
	// Entry<String, LinkedList<ParameInfo>> e = i.next();
	// String key = e.getKey();
	// LinkedList<ParameInfo> valueList = e.getValue();
	// String value = valueList.get(0).getValue();
	// pEdKey += ","+key+"="+value;
	// }
	// boolean isSearch = !pEdList.add(pEdKey);
	// if(pEdList.size()>=P_ED_LIST_SIZE) {
	// int i = 0;
	// List<String> al = new LinkedList<String>();
	// for(String key : pEdList) {
	// i++;
	// al.add(key);
	// if(i>=P_ED_LIST_SIZE/2) break;
	// }
	// pEdList.removeAll(al);
	// }
	// return isSearch;
	// }
	/**
	 * 获取参数替换的值
	 * 
	 * @param ui
	 * @param exeParams
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public synchronized static HashMap<String, LinkedList<ParameInfo>> getParamValues(
			Url ui, String exeParams) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		String[] exeParamArr = exeParams.split(",");
		HashMap<String, LinkedList<ParameInfo>> hm = new HashMap<String, LinkedList<ParameInfo>>();
		for (int i = 0; i < exeParamArr.length; i++) {
			String eType = getExecClass(exeParamArr[i]);
			String paramName = getParamName(exeParamArr[i]);
			if (paramHm.containsKey(paramName)
					&& paramHm.get(paramName) != null
					&& paramHm.get(paramName).size() != 0) {
				hm.put(exeParamArr[i], paramHm.get(paramName));
			} else {
				/*** 能过UrlInfo对象携带的参数进行取值 ***/
				if (eType == null || eType.equals("")) {
					if (ui.getData(paramName) == null)
						return null;
					LinkedList<ParameInfo> pl = new LinkedList<ParameInfo>();
					pl.add(new ParameInfo(paramName, ui.getData(paramName)));
					hm.put(exeParamArr[i], pl);
				}
				/*** 需要执行的操作 ***/
				else {
					replaceParameter ep = (replaceParameter) Class.forName(
							"com.syntun.replace." + eType + "replace")
							.newInstance();
					/*** 暂时情况下只能嵌套UrlInfo对像中包含的数据 ***/
					HashMap<String, String> uiData = ui.getData();
					String paramStrInfo = paramName;
					for (Iterator<Entry<String, String>> iter = uiData
							.entrySet().iterator(); iter.hasNext();) {
						Entry<String, String> entry = iter.next();
						if (paramStrInfo.indexOf("[" + entry.getKey() + "]") != -1)
							paramStrInfo = paramStrInfo.replaceAll("\\["
									+ entry.getKey() + "\\]", entry.getValue());
					}
					LinkedList<ParameInfo> pl = ep.execParame(paramStrInfo);
					if (pl == null)
						return null;
					hm.put(exeParamArr[i], pl);
					paramHm.put(exeParamArr[i], pl);
				}
			}
		}
		return hm;
	}

	/**
	 * 进行参数平行提换
	 */
	private synchronized static LinkedList<Url> parallelReplace(String pUrl,
			HashMap<String, LinkedList<ParameInfo>> hm, String[] exeParamArr,
			Url pUrlInfo, String sortId) {
		LinkedList<Url> ll = new LinkedList<Url>();
		boolean isAllReplaceParam = true;
		int j = 0;

		while (isAllReplaceParam) {
			boolean isReplaceParam = false;
			String rPurl = pUrl;
			HashMap<String, String> urlParamHm = new HashMap<String, String>();
			for (int i = 0; i < exeParamArr.length; i++) {
				if (j == 0
						&& (!hm.containsKey(exeParamArr[i]) || hm
								.get(exeParamArr[i]) == null)) {
					isAllReplaceParam = false;
					break;
				}
				LinkedList<ParameInfo> ls = hm.get(exeParamArr[i]);
				ParameInfo pi;
				if (j >= ls.size()) {
					pi = ls.get(ls.size() - 1);
				} else {
					pi = ls.get(j);
					isReplaceParam = true;
				}
				// String dataFunName = "";
				// synchronized (listPurlFiled) {
				// if (listPurlFiled.size() == 0) {
				// getPurlFiledList();
				// }
				// }
				// for (PurlFiledList p : listPurlFiled) {
				// if (p.getUrlGroup().equals(pUrlInfo.getPatternUrlId() + "")
				// && p.getGenerateSortId().equals(sortId + "")
				// && p.getFieldStr().equals(pi.getKey())) {
				// dataFunName = p.getFiledFun();
				// }
				// }
				String value = pi.getValue();
				// if (dataFunName != null && !dataFunName.equals("")) {
				// PurlFun pf = new PurlFun();
				// Method method = null;
				// try {
				// method = pf.getClass().getMethod(dataFunName,
				// String.class);
				// String s[] = value.split("=");
				// value = s[0]
				// + "="
				// + (String) method.invoke(pf,
				// s[1].replace("%253D", "="));
				// } catch (Exception e1) {
				// e1.printStackTrace();
				// }
				// }
				rPurl = rPurl.replace(exeParamArr[i], value);
				urlParamHm.put(pi.getKey(), value);
			}
			j++;
			if (isReplaceParam) {
				Url ui = new Url(rPurl, pUrlInfo);
				for (Iterator<Entry<String, String>> iter = urlParamHm
						.entrySet().iterator(); iter.hasNext();) {
					Entry<String, String> e = iter.next();
					ui.addData(e.getKey(), e.getValue());
				}
				ui.setSortId(Integer.valueOf(sortId));
				ui.setFatherUrlId(pUrlInfo.getFatherUrlId());
				ll.add(ui);
			} else
				break;
		}
		if (isAllReplaceParam)
			return ll;
		else
			return null;
	}

	/**
	 * 匹配出参数内容
	 * 
	 * @param exeParam
	 * @return
	 */
	private static String getParamName(String exeParam) {
		String getNameP = "\\{.*--(.*?)--\\}";
		Pattern p = Pattern.compile(getNameP);
		Matcher m = p.matcher(exeParam);
		if (m.find())
			return m.group(1);
		else
			return null;
	}

	/**
	 * 获得参数类型
	 * 
	 * @param exeParam
	 * @return
	 */
	private static String getExecClass(String exeParam) {
		String getNameP = "\\{([^-]*?)--(.*?)--\\}";
		Pattern p = Pattern.compile(getNameP);
		Matcher m = p.matcher(exeParam);
		if (m.find())
			return m.group(1).toUpperCase();
		else
			return null;
	}

	/**
	 * 测试用main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Url ui = new Url("www.163.com", 0);
		ui.addData("partNumber", "111+/A111==");
		ui.addData("catalogId", "211111===");
		ui.addData("categoryId", "30000");
		ui.addData("shop_code", "SN_001");
		ui.addData("endPageNum", "18");
		ui.setPatternUrlId(100003);
		ResolveWebPage.getRplaceMaps();
		try {
			LinkedList<Url> list = execParameter(
					"http://www.suning.com/emall/SNProductStatusView?storeId=10052&catalogId=10051&partNumber={--partNumber--}&cityId=9017&vendorCode={SR--100003-[shop_code]--}&_={S--SELECT UNIX_TIMESTAMP(NOW())*1000--}",
					ui,
					"{--partNumber--},{--shop_code--},{SR--100003-[shop_code]--},{S--SELECT UNIX_TIMESTAMP(NOW())*1000--}",
					"4");
			for (Url u : list) {
				u.getClass();
				System.out.println(u.getUrl());
			}
			System.out.println("-------------------------------------------");
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// LinkedHashSet<String> a = new LinkedHashSet<String>();
		// for (int i = 0; i < 10; i++) {
		// a.add(i + "");
		// }
		// for (String i : a) {
		// System.out.println(i);
		// }
		// a.remove(5 + "");
		// System.out.println("--------------");
		// for (String i : a) {
		// System.out.println(i);
		// }
	}
}
