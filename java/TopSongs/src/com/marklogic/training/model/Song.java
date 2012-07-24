package com.marklogic.training.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Song {

	private static final Logger logger = LoggerFactory.getLogger(Song.class);
	
	public Song() {}
	
	private String title = null;
	private String artist = null;
	private String uri = null;
	private String genres = null;
	private String weekending = null;
	private long totalweeks = 0;
	private String description = null;
	private String album = null;
	private String label = null;
	private String writers = null;
	private String producers = null;
	private String formats = null;
	private String lengths = null;
	private String weeks = null;
	private String albumimage = null;
	private Snippet[] snippets = null;
	private FacetDetails[] facets = null;
	private String facetname = null;
	
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
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri ) {
		String encoding = "UTF-8";
		try {
			this.uri = URLEncoder.encode(uri,encoding);
		} catch (UnsupportedEncodingException e) {
			logger.error("could not URL encode song uri " + uri + "to " + encoding + " exception caught " + e.toString());
			this.uri = uri;
		}
	}
	public String getPlainTextUri() {
		String encoding = "UTF-8";
		try {
			return URLDecoder.decode(this.uri,encoding);
		} catch (UnsupportedEncodingException e) {
			logger.error("could not URL encode song uri " + uri + "to " + encoding + " exception caught " + e.toString());
			return uri;
		}
	}
	
	public String getWeekending() {
		return weekending;
	}
	public void setWeekending(String weekEnding ) {
		this.weekending = weekEnding;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres ) {
		this.genres = genres;
	}
	public long getTotalweeks() {
		return totalweeks;
	}
	public void setTotalweeks(long totalWeeks ) {
		this.totalweeks = totalWeeks;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description ) {
		this.description = description;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album ) {
		this.album = album;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label ) {
		this.label = label;
	}
	public String getWriters() {
		return writers;
	}
	public void setWriters(String writers ) {
		this.writers = writers;
	}
	public String getProducers() {
		return producers;
	}
	public void setProducers(String producers ) {
		this.producers = producers;
	}
	public String getFormats() {
		return formats;
	}
	public void setFormats(String formats ) {
		this.formats = formats;
	}
	public String getLengths() {
		return lengths;
	}
	public void setLengths(String lengths ) {
		this.lengths = lengths;
	}
	public String getWeeks() {
		return weeks;
	}
	public void setWeeks(String weeks ) {
		this.weeks = weeks;
	}
	public String getAlbumimage() {
		return albumimage;
	}
	public void setAlbumimage(String albumimage ) {
		String encoding = "UTF-8";
		try {
			this.albumimage = URLEncoder.encode(albumimage,encoding);
		} catch (UnsupportedEncodingException e) {
			logger.error("could not URL encode album art uri " + albumimage + "to " + encoding + " exception caught " + e.toString());
			this.albumimage = albumimage;
		}
				
	}
	public Snippet[] getSnippets() {
		return snippets;
	}
	public void setSnippets(Snippet[] snips) {
		this.snippets = snips;
	}
	public FacetDetails[] getFacets() {
		return facets;
	}
	public void setFacets(FacetDetails[] facets) {
		this.facets = facets;
	}
	public String getFacetname() {
		return facetname;
	}
	public void setFacetname(String facetname ) {
		this.facetname = facetname;
	}

}
