package com.marklogic.training;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

/*
 * this class encapsulates the MarkLogic Search API
 */
public class Search {

	private static final String OPTIONS_NAME = "basic-options";
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
	public Song[] search(String arg) {
		
		logger.info("Performing search() with arg = "+(arg == ""?" EMPTY_STRING":arg) );
		
		
		// create a manager for searching
		QueryManager queryMgr = conn.getClient().newQueryManager();

		// create a search definition
		StringQueryDefinition querydef = queryMgr.newStringDefinition(OPTIONS_NAME);
		
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
		Song[] songs = new Song[docSummaries.length];
		int i = 0;
		for (MatchDocumentSummary docSummary: docSummaries) {
			// read constituent documents
			Song song = readSong(docSummary.getUri() );
			songs[i] = song;
			i++;
		}

		return songs;
		
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
		XMLDocumentManager docMgr = conn.getClient().newXMLDocumentManager();
		// create the handle
		DOMHandle readHandle = new DOMHandle();
		// read the document into the handle
		logger.info("About to read document "+uri);
		docMgr.read(uri, readHandle);
		// access the document content
		Document doc = readHandle.get();
		// get the root element (top-songs)
		String rootName = doc.getDocumentElement().getNodeName();
		String ns = doc.getBaseURI();
		logger.info("Root elememt "+ns+":"+rootName);
		
		song.setTitle(SongHelper.getTagValue("title", doc));
		logger.info("song title is " + song.getTitle() );

		song.setArtist(SongHelper.getTagValue("artist", doc));
		logger.info("song artist is " + song.getArtist() );

		song.setUri(uri);
		logger.info("song uri is " + song.getPlainTextUri() );
		
		song.setGenres(SongHelper.getGenres(doc));
		logger.info("song genres is/are " + song.getGenres() );
		 
		song.setWeekending(SongHelper.getWeekLastAttr(doc));
		logger.info("song last week in charts was " + song.getWeekending() );

		song.setTotalweeks(SongHelper.getNumberOfWeeks(doc));
		logger.info(" number of weeks at #1 was " + song.getTotalweeks() );

		song.setDescription(SongHelper.getSongDescription(doc,70)); 
		logger.info(" Song description " + song.getDescription() );
		
		return song; 
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
