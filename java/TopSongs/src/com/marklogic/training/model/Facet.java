package com.marklogic.training.model;


public class Facet {
	
	public Facet(FacetDetails[] facetvalues, String name) {
		this.facetvalues = facetvalues;
		this.name = name;
	}
	public FacetDetails [] facetvalues = null;
	public String name = null;
	
	public FacetDetails[] getFacetvalues() {
		return facetvalues;
	}
	public void setFacetvalues(FacetDetails[] facetvalues) {
		this.facetvalues = facetvalues;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
