package com.syntun.webget;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * url访问限制类
 * 
 * @author tuo
 * 
 */
public class UrlLimit {
	/**
	 * 平台id
	 */
	private String urlGroup;

	/**
	 * 要使用此规则的sort_id，可以多个用,号隔开
	 */
	private List<String> sortIdList = new ArrayList<String>();

	/**
	 * url的状态（入库或者查询的时候使用）
	 */
	private int status;

	/**
	 * 访问间隔，（毫秒）
	 */
	private int intervalTime;
	/**
	 * url地址被插入到
	 */
	private Long userInsertTime = System.currentTimeMillis();
	/**
	 * 当前是否可以进行将当当的url地址添加到正常解析的集合中
	 */
	private Boolean insertStatus = true;

	private UrlLimitThread urlLimitThread;

	/**
	 * 是否使用DNS(1是，0否)
	 */
	private int isDns;

	public int getIsDns() {
		return isDns;
	}

	public void setIsDns(int isDns) {
		this.isDns = isDns;
	}

	public UrlLimitThread getUrlLimitThread() {
		return urlLimitThread;
	}

	public void setUrlLimitThread(UrlLimitThread urlLimitThread) {
		this.urlLimitThread = urlLimitThread;
	}

	public String getUrlGroup() {
		return urlGroup;
	}

	public void setUrlGroup(String urlGroup) {
		this.urlGroup = urlGroup;
	}

	public List<String> getSortIdList() {
		return sortIdList;
	}

	public void setSortIdList(List<String> sortIdList) {
		this.sortIdList = sortIdList;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	public Long getUserInsertTime() {
		return userInsertTime;
	}

	public Boolean getInsertStatus() {
		return insertStatus;
	}

	/**
	 * 更新状态和插入时间
	 */
	public synchronized void updateStatusToTrue() {
		userInsertTime = System.currentTimeMillis();
		insertStatus = true;
	}

	/**
	 * 更新状态和插入时间
	 */
	public synchronized void updateStatusToFalse() {
		userInsertTime = System.currentTimeMillis();
		insertStatus = false;
	}

}
