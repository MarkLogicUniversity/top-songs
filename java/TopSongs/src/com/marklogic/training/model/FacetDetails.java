package com.marklogic.training.model;

public class FacetDetails {
	public FacetDetails(String name, long count) {
		this.name = name;
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	private String name = null;
	private long count = 0;
	

}
