package com.marklogic.training.jaxb;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "weeks", namespace = "http://marklogic.com/MLU/top-songs")
public class Weeks {
	
	private String last = null;
	private List<String> week;
	
	public Weeks() {
	}
	
	public String getLast() {
		return last;
	}
	@XmlAttribute
	public void setLast(String last) {
		this.last = last;
	}
	public List<String> getWeek() {
		return week;
	}
	public void setWeek(List<String> week) {
		this.week = week;
	}

	@Override
	public String toString() {
		return "Weeks [last=" + last + ", week=" + week + "]";
	}
	//comma separated list
	public String toCSL() {
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> i = week.iterator(); i.hasNext(); ) {
			sb.append(i.next());
			if (i.hasNext())
				sb.append(",");
		}
		return sb.toString();
	}

}
