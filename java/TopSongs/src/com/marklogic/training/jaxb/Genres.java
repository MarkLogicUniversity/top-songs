package com.marklogic.training.jaxb;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "genres", namespace = "http://marklogic.com/MLU/top-songs")
public class Genres {
	
	private List<String> genre;

	public Genres(List<String> genre) {
		this.genre = genre;
	}

	public List<String> getGenre() {
		return genre;
	}

	public void setGenre(List<String> genre) {
		this.genre = genre;
	}

	@Override
	public String toString() {
		return "Genres [genre=" + genre + "]";

	}
	public String toCSL() {
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> i = genre.iterator(); i.hasNext(); ) {
			sb.append(i.next());
			if (i.hasNext())
				sb.append(", ");
		}
		return sb.toString().toLowerCase();
		
	}

	public Genres() {
	}
	
	
	

}
