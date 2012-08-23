package com.marklogic.training.test;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.admin.NamespacesManager;
import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.admin.ServerConfigurationManager;
import com.marklogic.client.admin.config.QueryOptions.Facets;
import com.marklogic.client.admin.config.QueryOptions.FragmentScope;
import com.marklogic.client.admin.config.QueryOptions.QueryTransformResults;
import com.marklogic.client.admin.config.QueryOptions;
import com.marklogic.client.admin.config.QueryOptionsBuilder;
import com.marklogic.client.admin.config.support.Buckets;
import com.marklogic.client.admin.config.support.RangeIndexType;
import com.marklogic.client.admin.config.support.RangeSpec;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.QueryOptionsHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.io.SearchHandle.Report;
import com.marklogic.client.query.KeyValueQueryDefinition;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StructuredQueryBuilder;
import com.marklogic.client.query.StructuredQueryDefinition;
import com.marklogic.training.MarkLogicConnection;
import com.marklogic.training.admin.LoadOptions;

public class TestStructuredQuery {
	private static final Logger logger = LoggerFactory.getLogger(TestStructuredQuery.class);

	private static final String OPTIONS_NAME = "advanced-options";

	public static void buildAndLoadOptions() {
		try {
			// we are using admin-role credentials in order to write new query options
			// (note the properties filename)
			MarkLogicConnection conn =  new MarkLogicConnection("data/marklogic-admin.properties");
			
			try {
				// add top songs namespace to the server
				ServerConfigurationManager configManager = conn.getClient().newServerConfigManager();
				configManager.newNamespacesManager().updatePrefix("ts", "http://marklogic.com/MLU/top-songs");

				
				QueryOptionsManager qom =  conn.getClient().newServerConfigManager().newQueryOptionsManager();

				// Create a builder for constructing query configurations.
				QueryOptionsBuilder qob = new QueryOptionsBuilder();

				RangeSpec rs = qob.elementAttributeRangeIndex(new QName("http://marklogic.com/MLU/top-songs","weeks"), 
															  new QName("","last"), 
															  qob.rangeType(new QName("xs:date") )
																	  );
				RangeSpec art = qob.elementRangeIndex(new QName("http://marklogic.com/MLU/top-songs","artist"), 
						  							  qob.stringRangeType("http://marklogic.com/collation/en/S1/AS/T00BB")
						);

				RangeSpec gen = qob.elementRangeIndex(new QName("http://marklogic.com/MLU/top-songs","genre"), 
						  							  qob.stringRangeType("http://marklogic.com/collation/en/S1/AS/T00BB")
						);

				List<Buckets> buckets = createBuckets(qob);
				
				QueryOptionsHandle queryOptions = new QueryOptionsHandle()
						.withSortOrders(qob.sortOrder(rs,QueryOptions.QuerySortOrder.Direction.DESCENDING ))
						.withConstraints(
								qob.constraint("title", qob.value(qob.elementTermIndex(new QName("http://marklogic.com/MLU/top-songs","title")))),
								qob.constraint("genreSearch", qob.value(qob.elementTermIndex(new QName("http://marklogic.com/MLU/top-songs","genre")))),
								qob.constraint("creator", qob.word(qob.fieldTermIndex("creator"))),
								qob.constraint("artist", qob.range(art, 
																   Facets.FACETED, 
																   FragmentScope.DOCUMENTS, null, 
																   new String[]{"limit=30","frequency-order","descending"} ) 
										      ),
								qob.constraint("decade", qob.range(rs, 
														Facets.FACETED, 
														FragmentScope.DOCUMENTS, buckets, 
														new String[]{"limit=10","descending"} ) 
									    ),

								qob.constraint("genre", qob.range(gen, 
																Facets.FACETED, 
																FragmentScope.DOCUMENTS, null, 
																new String[]{"limit=20","frequency-order","descending"} ) 
												)
								      
										      
							);
							
						
				queryOptions.setDebug(true);
				queryOptions.setReturnFacets(true);
				// write the query options to the database
				qom.writeOptions(OPTIONS_NAME, queryOptions);		
				
				logger.info("wrote options to db");
				
				// create a handle to receive the query options
				StringHandle readHandle = new StringHandle();

				// read the query options from the database
				qom.readOptions(OPTIONS_NAME, readHandle);
				
				logger.info("Search Options" +" named "+ OPTIONS_NAME + " : \n" + readHandle.get());
				
				
			} catch (Exception e) {
				logger.error("Exception : " + e.toString() );
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw, true);
				e.printStackTrace(pw);
				pw.flush();
				sw.flush();
				logger.error( sw.toString() );
				
			} finally { 
				// don't leave a hanging connection in the case an exception is thrown in this try block
				if (conn.getClient() != null)
					conn.getClient().release();
			}
		
		} catch(Exception e) {
			logger.error("Exception :" + e.toString() );
		}

	}
	private static List<Buckets> createBuckets(QueryOptionsBuilder qob) {
		List<Buckets> b = new ArrayList<Buckets>();
		b.add( qob.bucket("2010s", "2010s", "2010-01-01", null) );
		b.add( qob.bucket("2000s", "2000s", "2000-01-01", "2010-01-01") );
		b.add( qob.bucket("1990s", "1990s", "1990-01-01", "2000-01-01") );
		b.add( qob.bucket("1980s", "1980s", "1980-01-01", "1990-01-01") );
		b.add( qob.bucket("1970s", "1970s", "1970-01-01", "1980-01-01") );
		b.add( qob.bucket("1960s", "1960s", "1960-01-01", "1970-01-01") );
		b.add( qob.bucket("1950s", "1950s", "1950-01-01", "1960-01-01") );
		b.add( qob.bucket("1940s", "1940s", null, "1950-01-01") );

		return b;
		
	}
		
	public static void testStructuredQuery(String arg) {
		try {

			MarkLogicConnection conn =  new MarkLogicConnection("data/marklogic.properties");
			
			try {
	
				QueryManager queryMgr = conn.getClient().newQueryManager();
								
				//set up element to be searched
				String constraintName = "title";
				
				// create a query builder for the query options
		        StructuredQueryBuilder qb = new StructuredQueryBuilder(OPTIONS_NAME);

		        // build match values of song title against the corresponding range index
		        // the constraint already exists in full options
		        StructuredQueryDefinition querydef = qb.valueConstraint(constraintName, arg);

				// create a handle for the search results
				SearchHandle resultsHandle = new SearchHandle();

				// run the search
				queryMgr.search(querydef, resultsHandle);
				logger.info(" search for documents constrained by title");
				
				logger.info("Matched "+resultsHandle.getTotalResults()+
						" documents in element 'title' of/containing '"+arg+"'\n");
				
				long count = resultsHandle.getTotalResults();
				if (count > 0) {
					MatchDocumentSummary[] docSummary = resultsHandle.getMatchResults();
					for (int i=0; i<(count >10?10:count); i++) {
						logger.info("retrieved song "+docSummary[i].getUri());
					}
				}
				
				logger.info(" search for documents constrained by genre");
				 
				constraintName = "genreSearch";
				arg = "dance-pop";
				StructuredQueryDefinition queryDef1 = qb.valueConstraint(constraintName, arg);
				queryMgr.search(queryDef1, resultsHandle);
				logger.info("Matched "+resultsHandle.getTotalResults()+
						" documents of  genre "+arg+"'\n");
				count = resultsHandle.getTotalResults();
				if (count > 0) {
					MatchDocumentSummary[] docSummary = resultsHandle.getMatchResults();
					for (int i=0; i<(count >10?10:count); i++) {
						logger.info("retrieved song "+docSummary[i].getUri());
					}
				}
				
				
				logger.info(" search for documents constrained by genre and title ");
				StructuredQueryDefinition qd = qb.and(querydef,queryDef1);
				queryMgr.search(qd, resultsHandle);
				logger.info("Matched "+resultsHandle.getTotalResults()+
						" documents of  combined genre and title ");
				count = resultsHandle.getTotalResults();
				if (count > 0) {
					MatchDocumentSummary[] docSummary = resultsHandle.getMatchResults();
					for (int i=0; i<(count >10?10:count); i++) {
						logger.info("retrieved song "+docSummary[i].getUri());
					}
				}
				
				logger.info(" search for documents constrained by creator field");
				
				constraintName = "creator";
				arg = "george martin";
				querydef = qb.wordConstraint("creator", arg);
				
				queryMgr.search(querydef, resultsHandle);
				logger.info("Matched "+resultsHandle.getTotalResults()+
						" documents with creator "+arg+"\n");
				count = resultsHandle.getTotalResults();
				if ( count > 0) {
					MatchDocumentSummary[] docSummary = resultsHandle.getMatchResults();
					for (int i=0; i< (count >10?10:count); i++) {
						logger.info("retrieved song "+docSummary[i].getUri());
					}
				}
				
				logger.info(" search for documents by ALL words");
				
				String arg1 = "late";
				String arg2 = "night";
				querydef = qb.and(qb.term(arg1),
								  qb.term(arg2)
								  );
				
				queryMgr.search(querydef, resultsHandle);
				logger.info("Matched "+resultsHandle.getTotalResults()+" documents with words "+arg1+" "+arg2+"\n");
				count = resultsHandle.getTotalResults();
				if ( count > 0) {
					MatchDocumentSummary[] docSummary = resultsHandle.getMatchResults();
					for (int i=0; i< (count >10?10:count); i++) {
						logger.info("retrieved song "+docSummary[i].getUri());
					}
				}
				logger.info(" search for documents by ANY words");
				
				arg1 = "late";
				arg2 = "night";
				querydef = qb.or(qb.term(arg1),
								  qb.term(arg2)
								  );
				
				queryMgr.search(querydef, resultsHandle);
				logger.info("Matched "+resultsHandle.getTotalResults()+" documents with some of the words "+arg1+" "+arg2+"\n");
				count = resultsHandle.getTotalResults();
				if ( count > 0) {
					MatchDocumentSummary[] docSummary = resultsHandle.getMatchResults();
					for (int i=0; i< (count >10?10:count); i++) {
						logger.info("retrieved song "+docSummary[i].getUri());
					}
				}
				logger.info(" search for documents by EXACT phrase");
				
				arg1 = "late";
				arg2 = "night";
				String arg3 = arg1 +" "+arg2;
				querydef = qb.term(arg3);
				
				queryMgr.search(querydef, resultsHandle);
				logger.info("Matched "+resultsHandle.getTotalResults()+" documents with exactly the words "+arg3+"\n");
				count = resultsHandle.getTotalResults();
				if ( count > 0) {
					MatchDocumentSummary[] docSummary = resultsHandle.getMatchResults();
					for (int i=0; i< (count >10?10:count); i++) {
						logger.info("retrieved song "+docSummary[i].getUri());
					}
				}
				
				
				logger.info(" search for documents by ALL words EXCEPT one");
				
				arg1 = "late";
				arg2 = "night";
				arg3 = "party";
				
				// problem with this approach - being investigated
				//querydef = qb.andNot(qb.and(qb.term(arg1), qb.term(arg2) ), qb.term(arg3) );
				
				querydef = qb.and(qb.and(qb.term(arg1), qb.term(arg2)), qb.not(qb.term(arg3)) );
				
				queryMgr.search(querydef, resultsHandle);
				logger.info("Matched "+resultsHandle.getTotalResults()+" documents with words "+arg1+" "+arg2+" and NOT " +arg3+"\n");
				count = resultsHandle.getTotalResults();
				if ( count > 0) {
					MatchDocumentSummary[] docSummary = resultsHandle.getMatchResults();
					for (int i=0; i< (count >10?10:count); i++) {
						logger.info("retrieved song "+docSummary[i].getUri());
					}
				}	
				
				logReports(resultsHandle);
		
				return;
				
			} catch (Exception e) {
				logger.error("Exception : " + e.toString() );
			} finally { 
				// don't leave a hanging connection in the case an exception is thrown in this try block
				if (conn.getClient() != null)
					conn.getClient().release();
			}
		
		} catch(Exception e) {
			logger.error("Exception :" + e.toString() );
		}
		
	}
	private static void logReports(SearchHandle resultsHandle) {
		Report[] reports = resultsHandle.getReports();
		if (reports != null) {
			for (int i=0; i<reports.length; i++) {
				if (reports[i].getMessage() != null)
					logger.info("Report message: "+reports[i].getMessage() );
			}
		}
		
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		buildAndLoadOptions();
		//testStructuredQuery("It's Too Late&quot; / &quot;I Feel the Earth Move");
		testStructuredQuery("Tik Tok");

	}
}
