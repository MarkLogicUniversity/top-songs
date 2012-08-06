package com.marklogic.training.jaxb;

import java.util.List;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "lengths", namespace = "http://marklogic.com/MLU/top-songs")
public class Lengths {
	private List<String> length;

	@Override
	public String toString() {
		return "Lengths [length=" + length + "]";
	}

	public Lengths() {
	}

	public List<String> getLength() {
		return length;
	}

	public void setLength(List<String> length) {
		this.length = length;
	}

	public Lengths(List<String> length) {
		this.length = length;
	}
	public String toCSL() {
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> i = length.iterator(); i.hasNext(); ) {
			sb.append(i.next());
			if (i.hasNext())
				sb.append(",");
		}
		return sb.toString();
		
	}

	
	
	
}
