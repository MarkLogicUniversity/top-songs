package com.marklogic.training.jaxb;

import java.util.List;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "writers", namespace = "http://marklogic.com/MLU/top-songs")
public class Writers {

	private List<String> writer;

	@Override
	public String toString() {
		return "Writers [writer=" + writer + "]";
	}

	public List<String> getWriter() {
		return writer;
	}

	public void setWriter(List<String> writer) {
		this.writer = writer;
	}

	public Writers() {
	}

	public Writers(List<String> writer) {
		this.writer = writer;
	}
	public String toCSL() {
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> i = writer.iterator(); i.hasNext(); ) {
			sb.append(i.next());
			if (i.hasNext())
				sb.append(",");
		}
		return sb.toString();
		
	}

	
}
