package com.marklogic.training;

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
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
