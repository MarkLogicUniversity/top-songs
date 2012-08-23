package com.marklogic.training;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.admin.ServerConfigurationManager;
import com.marklogic.client.admin.config.QueryOptions;
import com.marklogic.client.admin.config.QueryOptionsBuilder;
import com.marklogic.client.admin.config.support.RangeSpec;
import com.marklogic.client.document.BinaryDocumentManager;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.QueryOptionsHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.SearchHandle.Report;
import com.marklogic.client.query.FacetResult;
import com.marklogic.client.query.FacetValue;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.MatchLocation;
import com.marklogic.client.query.MatchSnippet;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import com.marklogic.client.query.StructuredQueryBuilder;
import com.marklogic.client.query.StructuredQueryDefinition;
import com.marklogic.client.query.ValuesDefinition;

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
	private static final String GENRE_ONLY = "genre-options";
	private static final String BIRTHDAY_QUERY = "birthday-query-options";
	private static final String ADVANCED_OPTIONS = "advanced-options";
	private static final String CONNECTION_ATTR_NAME = "com.marklogic.training.server.connection";
	private static final Logger logger = LoggerFactory.getLogger(Search.class);
	private MarkLogicConnection conn = null;
	private SongBuilder         songBuilder = null;
	
	public Search() {
		logger.info("initialising MarkLogic Search...");
		conn = new MarkLogicConnection();
		//songBuilder = new DOMSongBuilder(conn);
		songBuilder = new JAXBSongBuilder(conn); 
	}
	public void setConnection(MarkLogicConnection conn) {
		this.conn = conn;
		songBuilder.setConnection(conn);
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
	public SearchResults search(String arg, long start, String facetsOnly) {
		
		logger.info("Performing search() with arg = "+(arg == ""?" EMPTY_STRING":arg) );
		
		String options = null;
		
		if (facetsOnly == "TRUEALL")
			options = FACETS_ONLY;
		else if (facetsOnly == "TRUEGENRE")
			options = GENRE_ONLY;
		else
			options = FULL_OPTIONS;
		
		logger.info(" options uri set to "+ options);
		
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

		logReports(resultsHandle);
		Song[] songs = getSongs(resultsHandle);
		Facet[] facets = getFacets(resultsHandle);
		SearchResults results = new SearchResults(facets, songs, resultsHandle.getTotalResults(), queryMgr.getPageLength() );
		return results;
		
	}
	/*
	 * this method uses structured search 
	 * @param query : this is a structured query definition which was built as the user input was processed.
	 * 
	 */
	public SearchResults advancedSearch(StructuredQueryDefinition query) {
		logger.info("Performing advancedSearch() " );
		
		String options = ADVANCED_OPTIONS;
				
		// create a manager for searching
		QueryManager queryMgr = conn.getClient().newQueryManager();

		// create a handle for the search results
		SearchHandle resultsHandle = new SearchHandle();

		// set the page length
		queryMgr.setPageLength(10);
		// run the search
		queryMgr.search(query, resultsHandle);
		
		logger.info("Advanced Search Matched "+resultsHandle.getTotalResults() );

		logReports(resultsHandle);
		Song[] songs = getSongs(resultsHandle);
		Facet[] facets = getFacets(resultsHandle);
		SearchResults results = new SearchResults(facets, songs, resultsHandle.getTotalResults(), queryMgr.getPageLength() );
		return results;
		
	}


	public String getBirthdaySong(String date) {
		
		// create a manager for searching
		QueryManager queryMgr = conn.getClient().newQueryManager();

		// create a query builder for the query options
       StructuredQueryBuilder qb = new StructuredQueryBuilder(BIRTHDAY_QUERY);
       
        // build a range query
       StructuredQueryDefinition querydef = qb.rangeConstraint("week", StructuredQueryBuilder.Operator.GE, date);

       // create a handle for the search results
       SearchHandle resultsHandle = new SearchHandle();

       // run the search
       queryMgr.search(querydef, resultsHandle);
       logReports(resultsHandle);
       // iterate over the result documents
       MatchDocumentSummary[] docSummaries = resultsHandle.getMatchResults();
       logger.info("Listing "+docSummaries.length+" documents:\n");
       for (int i=0; i<docSummaries.length ; i++)  {
    	   logger.debug("retrieved the following document "+docSummaries[i].getUri());
       }
       if (docSummaries.length >= 1) {
    	   String uri = docSummaries[0].getUri();
    	   logger.info("birthday song is"+uri);
    	   return uri;
			
       }
      
       return "";
	}
	/*
	 * this method invokes the REST extension which in turn calls search:search() on the server
	 * it is called to fill the autocomplete suggestion list.
	 */
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
	 * 
	 */
	public void insertSong(String title, String artist, String album, String genres, String writers, 
							String producers, String label, String description,String weeks) {
		songBuilder.insertSong(title, artist, album, genres, writers, producers, label, description, weeks);
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
	
	/*
	 * retrieve the facets and place them in a facet array.
	 */
	private Facet[] getFacets(SearchHandle results) {
		
		List<Facet> fl = new ArrayList<Facet>();
		// retrieve facet results from search client
		FacetResult[] facetResults = results.getFacetResults();
		if (facetResults == null) 
			return null;
		
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
		return facets;

	}
	private Song[] getSongs(SearchHandle resultsHandle) {
		// all matched documents
		MatchDocumentSummary[] docSummaries = resultsHandle.getMatchResults();
		logger.info("Listing "+docSummaries.length+" documents");
		
		Song[] songs = new Song[docSummaries.length];
		if (docSummaries.length >0) {
			int i = 0;
			for (MatchDocumentSummary docSummary: docSummaries) {
							
				Snippet[] snips = getSnippets(docSummary);
				
				// read constituent documents			
				Song song = songBuilder.getSong(docSummary.getUri() );
				logger.debug("about to set snippets in song ");
	
				song.setSnippets(snips);
				songs[i] = song;
				i++;
			}
		}
		return songs;

	}
	private Snippet[] getSnippets(MatchDocumentSummary docSummary) {
		MatchLocation[] matches = docSummary.getMatchLocations();
		
		List<Snippet> snippetList = new ArrayList<Snippet>();
		
		if (matches.length > 0) {
			for (MatchLocation match: matches) {
				
				MatchSnippet[] snippets = match.getSnippets();
				
				for (MatchSnippet snippet: snippets) {
					
					snippetList.add(new Snippet(snippet.getText(), snippet.isHighlighted()) );
					
					logger.debug(" snippet text is "+(snippet.isHighlighted()?" highlighted ":" NOT highlighted ")+" text value: "+snippet.getText());
				}
				
			}
		}
		
		logger.debug("gathered number of snippets = "+snippetList.size());
		Snippet[] snips = new Snippet[snippetList.size()];
		snippetList.toArray(snips);
		return snips;

	}
	/*
	 * log any debug messages that are returned from the search 
	 * e.g. how the search has been resolved in cts:search
	 * 
	 */
	private void logReports(SearchHandle resultsHandle) {
		Report[] reports = resultsHandle.getReports();
		if (reports != null) {
			for (int i=0; i<reports.length; i++) {
				if (reports[i].getMessage() != null)
					logger.info("Report message: "+reports[i].getMessage() );
			}
		}
		
		
	}
}
