package com.syntun.tools;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternMatcher {
	/**
	 * 内容长度=>md5 key
	 */
	private Map<Integer, Map<String, List<String[]>>> lCacheMap = new HashMap<Integer, Map<String, List<String[]>>>();
	/**
	 * 当前匹配结果的缓存key
	 */
	private String key;
	/**
	 * 储存正则表达式对像 patternStr>Pattern
	 */
	private Map<String, Pattern> strPattern = new HashMap<String, Pattern>();
	
	/**
	 * 用来获取子正则的正则字符串
	 */
	private final String GET_CHILD_PATTERN_STR = "\\{\\!(\\d*)--(.*?)--\\}";
	/**
	 * 获取子正则的正则对象
	 */
	private final Pattern GET_CHILD_PATTERN = Pattern.compile(GET_CHILD_PATTERN_STR);
	
	public void setReset() {
		lCacheMap.clear();
	}
	
	public List<String[]> find(String patternStr, String content) {
		if(patternStr.indexOf("{!")!=-1) {
			Matcher m = GET_CHILD_PATTERN.matcher(patternStr);
			Map<String, List<String>> childPValues = new HashMap<String, List<String>>();
			int pLength = 0;
			while(m.find()) {
				List<String> values = this.findChildPatternValue(m.group(2), Integer.parseInt(m.group(1)), content);
				if(values.size()>pLength) pLength = values.size();
				childPValues.put(m.group(0), values);
			}
			List<String[]> result = new ArrayList<String[]>();
			for(int i=0; i<pLength; i++) {
				String exePatternStr = patternStr;
				for(Iterator<Entry<String, List<String>>> iter = childPValues.entrySet().iterator();iter.hasNext() ;) {
					Entry<String, List<String>> e = iter.next();
					List<String> values = e.getValue();
					exePatternStr = exePatternStr.replace(e.getKey(), values.size()>i?values.get(i):values.get(values.size()-1));
				}
				result.addAll(this.exeFind(exePatternStr, content));
			}
			return result;
		}
		else {
			return this.exeFind(patternStr, content);
		}
	}
	/**
	 * 匹配子匹配,如果匹配不上将返回空字符串作为值
	 * @param patternStr
	 * @param groupNum
	 * @param content
	 * @return
	 */
	private List<String> findChildPatternValue(String patternStr, int groupNum, String content) {
		List<String[]> findResult = this.exeFind(patternStr, content);
		List<String> valueList = new ArrayList<String>();
		for(String[] values : findResult) {
			valueList.add(values[groupNum]);
		}
		if(valueList.size()==0) valueList.add("");
		return valueList;
	}
	/**
	 * @param patternStr	正则表达式的字符串
	 * @param content		匹配的内容
	 */
	private List<String[]> exeFind(String patternStr, String content) {
		//List<String[]> result = this.getCache(patternStr, content);
		List<String[]> result = null;
		if(result==null) {
			result = new ArrayList<String[]>();
			Pattern p;
			if(strPattern.containsKey(patternStr)) {
				p = strPattern.get(patternStr);
			}
			else {
				p = Pattern.compile(patternStr);
				strPattern.put(patternStr, p);
			}
			Matcher m = p.matcher(content);
			while(m.find()) {
				new HashMap<Integer, String>();
				int groupCount = m.groupCount()+1;
				String[] reArr = new String[groupCount];
				for(int groupNum=0; groupNum<groupCount; groupNum++) {
					reArr[groupNum] = m.group(groupNum);
				}
				result.add(reArr);
			}
			int len = content.length();
			Map<String, List<String[]>> re;
			if(!lCacheMap.containsKey(len)) re = new HashMap<String,List<String[]>>();
			else re = lCacheMap.get(len);
			re.put(key, result);
			lCacheMap.put(len, re);
		}
		return result;
	}
//	/**
//	 * 是否已缓存了解析
//	 * 
//	 * @param patternStr
//	 * @param content
//	 * @return
//	 */
//	private List<String[]> getCache(String patternStr, String content) {
//		int strLen = content.length();
//		if(lCacheMap.containsKey(strLen)) {
//			if(strLen<1500)
//				key = new GetMD5().mD5Code(patternStr+"\n\n"+content);
//			else
//				key = new GetMD5().mD5Code(patternStr+"\n\n"+strLen);
//			if(lCacheMap.get(strLen).containsKey(key)) return lCacheMap.get(strLen).get(key);
//			else return null;
//		} 
//		else return null;
//	}
}
