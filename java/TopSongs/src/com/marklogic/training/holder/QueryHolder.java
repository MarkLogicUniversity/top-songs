package com.marklogic.training.holder;

import com.marklogic.client.query.StructuredQueryDefinition;

public class QueryHolder {
	private String stringQuery = null;
	private StructuredQueryDefinition structuredQuery = null;
	public QueryHolder(String stringQuery,
			StructuredQueryDefinition structuredQuery) {
		this.stringQuery = stringQuery;
		this.structuredQuery = structuredQuery;
	}
	public String getStringQuery() {
		return stringQuery;
	}
	public void setStringQuery(String stringQuery) {
		this.stringQuery = stringQuery;
	}
	public StructuredQueryDefinition getStructuredQuery() {
		return structuredQuery;
	}
	public void setStructuredQuery(StructuredQueryDefinition structuredQuery) {
		this.structuredQuery = structuredQuery;
	}
}
