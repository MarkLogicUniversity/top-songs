package com.marklogic.training.jaxb;

import java.io.StringWriter;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


@XmlRootElement(name = "top-song", namespace="http://marklogic.com/MLU/top-songs")
public class Topsong {
	
	public Topsong() {
		super();
	}
	private String title;
	private String artist;

	private Weeks weeks;
	
	private Album album;
	private String released;
	
	private Formats formats;
	private String recorded;
	
	private Genres genres;
	private Lengths lengths;
	private String label;
	
	private Writers writers;
	
	private Producers producers;

	private Element descr;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title ) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist ) {
		this.artist = artist;
	}
	@XmlAnyElement
	public Element getDescr() {
		return descr;
	}
	public void setDescr(Element description ) {
		this.descr = description;
	}
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album ) {
		this.album = album;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label ) {
		this.label = label;
	}
	public Weeks getWeeks() {
		return weeks;
	}
	public void setWeeks(Weeks weeks) {
		this.weeks = weeks;
	}
	@Override
	public String toString() {
		return "Topsong [title=" + title + ", artist=" + artist + ", weeks="
				+ weeks + ", album=" + album + ", released=" + released
				+ ", formats=" + formats + ", recorded=" + recorded
				+ ", genres=" + genres + ", lengths=" + lengths + ", label="
				+ label + ", writers=" + writers + ", producers=" + producers
				+ ", descr=" + stringifyDescr() + "]";
	}
	public String getReleased() {
		return released;
	}
	public void setReleased(String released) {
		this.released = released;
	}
	public Formats getFormats() {
		return formats;
	}
	public void setFormats(Formats formats) {
		this.formats = formats;
	}
	public String getRecorded() {
		return recorded;
	}
	
	public void setRecorded(String recorded) {
		this.recorded = recorded;
	}
	public Genres getGenres() {
		return genres;
	}
	public void setGenres(Genres genres) {
		this.genres = genres;
	}
	public Lengths getLengths() {
		return lengths;
	}
	public void setLengths(Lengths lengths) {
		this.lengths = lengths;
	}
	public Writers getWriters() {
		return writers;
	}

	public void setWriters(Writers writers) {
		this.writers = writers;
	}
	public Producers getProducers() {
		return producers;
	}
	
	public void setProducers(Producers producers) {
		this.producers = producers;
	}
	public String stringifyDescr() {
	    String xmlString = null;

	    try
	    {
	      // Set up the output transformer
	      TransformerFactory transfac = TransformerFactory.newInstance();
	      Transformer trans = transfac.newTransformer();
	      trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	      trans.setOutputProperty(OutputKeys.INDENT, "no");

	      // Print the DOM node

	      StringWriter sw = new StringWriter();
	      StreamResult result = new StreamResult(sw);
	      DOMSource source = new DOMSource(descr);
	      trans.transform(source, result);
	      xmlString = sw.toString();
	     
	      
	    }
	    catch (TransformerException e)
	    {
	      xmlString = "description not found";
	    }

		return xmlString;
	}

}
