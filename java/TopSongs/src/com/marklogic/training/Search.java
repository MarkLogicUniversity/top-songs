package com.marklogic.training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.MatchLocation;
import com.marklogic.client.query.MatchSnippet;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

/*
 * this class encapsulates the MarkLogic Search API
 */
public class Search {

	private static final Logger logger = LoggerFactory.getLogger(Search.class);
	private static MarkLogicConnection conn = null;
	
	public Search() {
		logger.info("initialising MarkLogic Search...");
		conn = MarkLogicConnection.getInstance();
		
	}
	/*
	 * main search function
	 * @arg is the search argument passed from the browser
	 * 
	 * 1. set the search options
	 * 2. execute the search
	 * 3. parse the results into a tree
	 * 4. create a POJO with the results for processing in the view.
	 */
	public MatchDocumentSummary[] search(String arg) {
		
		logger.info("Performing search() with arg = "+(arg == ""?" EMPTY_STRING":arg) );
		
		// create a manager for searching
		QueryManager queryMgr = conn.getClient().newQueryManager();

		// create a search definition
		StringQueryDefinition querydef = queryMgr.newStringDefinition(null);
		
		// set the search argument
		querydef.setCriteria(arg);

		// create a handle for the search results
		SearchHandle resultsHandle = new SearchHandle();

		// set the page length
		queryMgr.setPageLength(10);
		// run the search
		queryMgr.search(querydef, resultsHandle);
		
		logger.info("Matched "+resultsHandle.getTotalResults()+
				" documents with '"+querydef.getCriteria()+"'\n");

		// iterate over the result documents
		MatchDocumentSummary[] docSummaries = resultsHandle.getMatchResults();
		logger.info("Listing "+docSummaries.length+" documents:\n");
		for (MatchDocumentSummary docSummary: docSummaries) {

			// iterate over the match locations within a result document
			MatchLocation[] locations = docSummary.getMatchLocations();
			logger.info("Matched "+locations.length+" locations in "+docSummary.getUri()+":");
			for (MatchLocation location: locations) {

				// iterate over the snippets at a match location
				for (MatchSnippet snippet : location.getSnippets()) {
					boolean isHighlighted = snippet.isHighlighted();

					if (isHighlighted)
						logger.info("["+snippet.getText()+"]");
				
				}
				
			}
		}


		return docSummaries;
		
	}
	public void stop() {
		logger.info("releasing MarkLogic Search...");
		if (conn != null) {
			conn.release();			
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
