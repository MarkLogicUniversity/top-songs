package com.marklogic.training.jaxb;

import java.util.List;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "producers", namespace = "http://marklogic.com/MLU/top-songs")
public class Producers {

	private List<String> producer;

	@Override
	public String toString() {
		return "Producers [producer=" + producer + "]";
	}

	public List<String> getProducer() {
		return producer;
	}

	public void setProducer(List<String> producer) {
		this.producer = producer;
	}

	public Producers(List<String> producer) {
		this.producer = producer;
	}

	public Producers() {
	}
	public String toCSL() {
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> i = producer.iterator(); i.hasNext(); ) {
			sb.append(i.next());
			if (i.hasNext())
				sb.append(",");
		}
		return sb.toString();
		
	}

	
}
