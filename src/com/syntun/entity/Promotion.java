package com.syntun.entity;

public class Promotion {
	private String database;
	private String proname;
	private String date;

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getProname() {
		return proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Promotion(String database, String proname, String date) {
		super();
		this.database = database;
		this.proname = proname;
		this.date = date;
	}

}
