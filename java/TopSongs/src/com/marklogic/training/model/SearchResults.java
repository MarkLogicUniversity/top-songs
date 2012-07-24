package com.marklogic.training.model;

public class SearchResults {
	
	public SearchResults(Facet[] facets, Song[] songs) {
		this.facets = facets;
		this.songs = songs;
	}
	public Facet[] facets = null;
	public Song[] songs = null;
	
	public Facet[] getFacets() {
		return facets;
	}
	public void setFacets(Facet[] facets) {
		this.facets = facets;
	}
	public Song[] getSongs() {
		return songs;
	}
	public void setSongs(Song[] songs) {
		this.songs = songs;
	}
	
}
