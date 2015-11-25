package com.syntun.entity;

public class CreateDataTime {
	private String databaseTable;
	private int createDataSum;
	private int tableId;
	private int addCount;

	public String getDatabaseTable() {
		return databaseTable;
	}

	public void setDatabaseTable(String databaseTable) {
		this.databaseTable = databaseTable;
	}

	public int getCreateDataSum() {
		return createDataSum;
	}

	public void setCreateDataSum(int createDataSum) {
		this.createDataSum = createDataSum;
	}

	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public int getAddCount() {
		return addCount;
	}

	public void setAddCount(int addCount) {
		this.addCount = addCount;
	}

}
