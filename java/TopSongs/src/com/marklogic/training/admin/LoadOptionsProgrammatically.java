package com.marklogic.training.admin;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.admin.ServerConfigurationManager;
import com.marklogic.client.admin.config.QueryOptions;
import com.marklogic.client.admin.config.QueryOptionsBuilder;
import com.marklogic.client.admin.config.QueryOptions.Facets;
import com.marklogic.client.admin.config.QueryOptions.FragmentScope;
import com.marklogic.client.admin.config.support.Buckets;
import com.marklogic.client.admin.config.support.RangeSpec;
import com.marklogic.client.io.QueryOptionsHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.query.QueryManager;
import com.marklogic.training.MarkLogicConnection;
/*
 * This class demonstrates building options with the Java API and writing them to MarkLogic Server.
 * These particular options are needed for the Birthday search box.
 * 
 */
public class LoadOptionsProgrammatically {


	private static final String CONSTRAINT_NAME = "week";

	private static final Logger logger = LoggerFactory.getLogger(LoadOptionsProgrammatically.class);

	private static MarkLogicConnection conn = null;

	private static void buildAndLoadBDayOptions(String options) {
		try {
			// we are using admin-role credentials in order to write new query options
			// (note the properties filename)
			conn =  new MarkLogicConnection("data/marklogic-admin.properties");
			
			try {
				// add top songs namespace to the server
				ServerConfigurationManager configManager = conn.getClient().newServerConfigManager();
				configManager.newNamespacesManager().updatePrefix("ts", "http://marklogic.com/MLU/top-songs");

				
				QueryOptionsManager qom =  conn.getClient().newServerConfigManager().newQueryOptionsManager();

				// Create a builder for constructing query configurations.
				QueryOptionsBuilder qob = new QueryOptionsBuilder();
				RangeSpec rs = qob.elementRangeIndex(new QName("http://marklogic.com/MLU/top-songs","week"), qob.rangeType(new String("xs:date")) );
				
				// create the query options - a simple range constraint and a sort order.
				QueryOptionsHandle queryOptions = new QueryOptionsHandle()
					.withSortOrders(qob.sortOrder(rs, QueryOptions.QuerySortOrder.Direction.ASCENDING))
					.withConstraints(
						qob.constraint(CONSTRAINT_NAME,
								qob.range(rs)
										));

				// write the query options to the database
				qom.writeOptions(options, queryOptions);		
				
				logger.info("wrote options to db");
				
				// create a handle to receive the query options
				StringHandle readHandle = new StringHandle();

				// read the query options from the database
				qom.readOptions(options, readHandle);
				
				logger.info("Search Options" +" named "+ options + " : \n" + readHandle.get());
				
				
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
	public static void buildAndLoadAdvOptions(String options) {
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
															  qob.rangeType(new String("xs:date") )
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
				qom.writeOptions(options, queryOptions);		
				
				logger.info("wrote options to db");
				
				// create a handle to receive the query options
				StringHandle readHandle = new StringHandle();

				// read the query options from the database
				qom.readOptions(options, readHandle);
				
				logger.info("Search Options" +" named "+ options + " : \n" + readHandle.get());
				
				
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
		

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		buildAndLoadBDayOptions("birthday-query-options");
		buildAndLoadAdvOptions("advanced-options");
	}

}
