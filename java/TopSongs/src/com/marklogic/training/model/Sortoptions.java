package com.marklogic.training.model;

public class Sortoptions {
	public Sortoptions(String option, boolean selected) {
		this.option = option;
		this.selected = selected;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	private String option = null;
	private boolean selected = false;
	
}
