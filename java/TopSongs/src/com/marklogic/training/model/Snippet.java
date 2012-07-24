package com.marklogic.training.model;
/*
 * snippeting information to presentation layer
 */
public class Snippet {
	public Snippet() {};
	public Snippet(String text, boolean is) {
		this.text = text;
		this.ishighlighted = is;
	}
	public String text = null;
	public boolean ishighlighted = false;
	public String getText() {
		return this.text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean getIshighlighted() {
		return this.ishighlighted;
	}
	public void setIshighlighted(boolean ishighlighted) {
		this.ishighlighted = ishighlighted;
	}


}
