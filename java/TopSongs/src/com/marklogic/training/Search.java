package com.marklogic.training;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.marklogic.client.document.BinaryDocumentManager;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.FacetResult;
import com.marklogic.client.query.FacetValue;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.MatchLocation;
import com.marklogic.client.query.MatchSnippet;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

import com.marklogic.training.jaxb.JAXBSongBuilder;
import com.marklogic.training.model.Facet;
import com.marklogic.training.model.SearchResults;
import com.marklogic.training.model.Snippet;
import com.marklogic.training.model.Song;
import com.marklogic.training.model.FacetDetails;
import com.marklogic.training.resourceext.SuggestionsManager;

/*
 * this class encapsulates the MarkLogic Search API
 */
public class Search {

	private static final String FULL_OPTIONS = "full-options";
	private static final String FACETS_ONLY = "facets-only-full-options";
	private static final String SUGGESTIONS_ONLY = "suggestion-options";
	private static final Logger logger = LoggerFactory.getLogger(Search.class);
	private static MarkLogicConnection conn = null;
	private SongBuilder songBuilder = null;
	
	public Search() {
		logger.info("initialising MarkLogic Search...");
		conn = MarkLogicConnection.getInstance();
		//songBuilder = new DOMSongBuilder(conn);
		songBuilder = new JAXBSongBuilder(conn); 
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
	public SearchResults search(String arg, long start, boolean facetsOnly) {
		
		logger.info("Performing search() with arg = "+(arg == ""?" EMPTY_STRING":arg) );
		
		String options;
		if (facetsOnly)
			options = FACETS_ONLY;
		else
			options = FULL_OPTIONS;
			
		
		// create a manager for searching
		QueryManager queryMgr = conn.getClient().newQueryManager();

		// create a search definition
		StringQueryDefinition querydef = queryMgr.newStringDefinition(options);
		
		// set the search argument
		querydef.setCriteria(arg);

		// create a handle for the search results
		SearchHandle resultsHandle = new SearchHandle();

		// set the page length
		queryMgr.setPageLength(10);
		// run the search
		queryMgr.search(querydef, resultsHandle, start);
		
		logger.info("Matched "+resultsHandle.getTotalResults()+
				" documents with '"+querydef.getCriteria()+"'\n");

		List<Facet> fl = new ArrayList<Facet>();
		// retrieve facet results from search client
		FacetResult[] facetResults = resultsHandle.getFacetResults();	
		logger.info("returned data on "+facetResults.length+" facets " );
		
		for (FacetResult facetResult: facetResults) {

			List<FacetDetails> fdl = new ArrayList<FacetDetails>();

			String facetName = facetResult.getName();
			logger.info(" Results for facet "+facetName);
			
			FacetValue[] facetValues = facetResult.getFacetValues();
			for (FacetValue facetValue: facetValues) {
				logger.debug(" Facet Value Label:"+facetValue.getLabel()+" Name:"+facetValue.getName()+" Count:"+facetValue.getCount());
				// now create and fill the model FacetDetails type
				FacetDetails values = new FacetDetails( facetValue.getName(),facetValue.getCount() );
				fdl.add(values);
			}
			// now create and fill the model array of facet details
			FacetDetails[] facetDetails = new FacetDetails[fdl.size()];
			fdl.toArray(facetDetails);
			Facet f = new Facet(facetDetails, facetName);
			fl.add(f);

			
		}
		// create the model object Facet to contain the FacetResults
		Facet[] facets = new Facet[fl.size()];
		fl.toArray(facets);


		// all matched documents
		MatchDocumentSummary[] docSummaries = resultsHandle.getMatchResults();
		logger.info("Listing "+docSummaries.length+" documents");
		
		Song[] songs = new Song[docSummaries.length];
		int i = 0;
		for (MatchDocumentSummary docSummary: docSummaries) {
						
			MatchLocation[] matches = docSummary.getMatchLocations();
					
			List<Snippet> snippetList = new ArrayList<Snippet>();
			
			for (MatchLocation match: matches) {
				
				MatchSnippet[] snippets = match.getSnippets();
				
				for (MatchSnippet snippet: snippets) {
					
					snippetList.add(new Snippet(snippet.getText(), snippet.isHighlighted()) );
					
					logger.debug(" snippet text is "+(snippet.isHighlighted()?" highlighted ":" NOT highlighted ")+" text value: "+snippet.getText());
				}
				
			}
			
			logger.debug("gathered number of snippets = "+snippetList.size());
			Snippet[] snips = new Snippet[snippetList.size()];
			snippetList.toArray(snips);
			
			// read constituent documents			
			Song song = songBuilder.getSong(docSummary.getUri() );
			logger.debug("about to set snippets in song ");

			song.setSnippets(snips);
			songs[i] = song;
			i++;
		}
		SearchResults results = new SearchResults(facets, songs, resultsHandle.getTotalResults(), queryMgr.getPageLength() );
		return results;
		
	}
	public String suggest(String q) {
		logger.info("suggest called with q "+q);
		SuggestionsManager sm = new SuggestionsManager(conn );

		return sm.suggest(q);
	}
	/*
	 * retrieve the details for one song
	 */
	public Song getSongDetails(String uri) {
		return songBuilder.getSongDetails(uri );
	}
	/*
	 * serve images from MarkLogic
	 */
	public InputStream serveImage(String uri) {
		
		// Read Document into an InputStream
		// create  handle
		InputStreamHandle handle = new InputStreamHandle();  

		try {				
			// create a manager for binary documents
			BinaryDocumentManager docMgr = conn.getClient().newBinaryDocumentManager();			
			
			// read document
			docMgr.read(uri,handle);
			// output the string
			logger.info("loaded binary document from db  - mimetype" + handle.getMimetype() );
				  
		} catch (Exception e) {
			logger.error("Exception : " + e.toString() );
		} 
		return  handle.get();
	}
	public void stop() {
		logger.info("releasing MarkLogic Search...");
		if (conn != null) {
			conn.release();			
		}
	}  

}
