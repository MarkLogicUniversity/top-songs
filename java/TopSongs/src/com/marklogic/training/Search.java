package com.marklogic.training;

import org.jdom2.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.example.handle.JDOMHandle;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.QueryOptionsHandle;
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
	private static final String OPTIONS_BASIC = "BASIC-OPTIONS";
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
		
		/* need admin authority to perform this 	 		
		 
		// create a manager for writing query options
		QueryOptionsManager optionsMgr = conn.getClient().newServerConfigManager().newQueryOptionsManager();

		// create some options
		QueryOptionsHandle handle = new QueryOptionsHandle();
		// see the query in the MarkLogic log
		handle.setDebug(true);
		// write them to the db
		optionsMgr.writeOptions(OPTIONS_BASIC, handle);
		*/
		
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
			// read constituent documents
			Song song = readSong(docSummary.getUri() );
			
			// iterate over the match locations within a result document
			MatchLocation[] locations = docSummary.getMatchLocations();
			logger.info("Matched "+locations.length+" locations in "+docSummary.getUri()+":");
		}

		return docSummaries;
		
	}
	public void stop() {
		logger.info("releasing MarkLogic Search...");
		if (conn != null) {
			conn.release();			
		}
	}
	
	private Song readSong(String uri) {
		// create the POJO
		Song song = new Song();
		// set up the read
		logger.info("1 ");
		XMLDocumentManager docMgr = conn.getClient().newXMLDocumentManager();
		// create metadata
		DocumentMetadataHandle metadataHandle = new DocumentMetadataHandle();
		// set the document format
		metadataHandle.setFormat(Format.XML);
		// create the handle
		logger.info("2 ");
		JDOMHandle readHandle = new JDOMHandle();
		// read the document into the handle
		logger.info("3 ");
		docMgr.read(uri, metadataHandle, readHandle);
		// access the document content
		logger.info("4 ");
		Document doc = readHandle.get();
		// 
		logger.info("5 ");
		String rootName = doc.getRootElement().getName();
		logger.info(rootName);

		
		return song;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
