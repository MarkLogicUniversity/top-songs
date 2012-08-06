package com.marklogic.training.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "album", namespace = "http://marklogic.com/MLU/top-songs")
public class Album {
	
	
	private String album = null;
	private String uri = null;
	

	public String getAlbum() {
		return album;
	}
	@XmlValue
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getUri() {
		return uri;
	}
	@XmlAttribute
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public Album(String album, String uri) {
		this.album = album;
		this.uri = uri;
	}
	
	public Album() {
	}
	@Override
	public String toString() {
		return "Album [album=" + album + ", uri=" + uri + "]";
	}
	

}
