package com.marklogic.training.test;

import java.io.InputStream;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.admin.NamespacesManager;
import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.admin.config.QueryOptions.QueryTransformResults;
import com.marklogic.client.admin.config.QueryOptionsBuilder;
import com.marklogic.client.admin.config.support.RangeIndexType;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.QueryOptionsHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.query.KeyValueQueryDefinition;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StructuredQueryBuilder;
import com.marklogic.client.query.StructuredQueryDefinition;
import com.marklogic.training.LoadOptions;
import com.marklogic.training.MarkLogicConnection;

public class TestStructuredQuery {
	private static final Logger logger = LoggerFactory.getLogger(TestStructuredQuery.class);

	private static final String OPTIONS_NAME = "suggestion-options";
	private static final String OPTIONS_FILENAME = "data/suggestions-options.xml";	

	/*
	 * Important to note that when reading options from a file you can use any normal handle - there is no need to use the QueryOptionsHandle.
	 * Just need to make sure that the QueryOptionsManager is used to write it to the database.
	 */
	public static void load(boolean isSourceFile) {
		try {
			// we are using admin-role credentials in order to write new query options
			// (note the properties filename)
			MarkLogicConnection conn =  MarkLogicConnection.getInstance("data/marklogic-admin.properties");
			
			try {
				
				// need admin authority to perform this 	 					 
				// create a manager for writing query options
				QueryOptionsManager optionsMgr = conn.getClient().newServerConfigManager().newQueryOptionsManager();
				
				if (isSourceFile) {
				    // build options - FROM FILE
					logger.info("set handle from byte[]");
					InputStreamHandle ish = readOptionsFromFile();
					logger.info("write following options to db :\n"+ish.toString());				
					optionsMgr.writeOptions(OPTIONS_NAME, ish);	

				} else {
					// build options - FROM CODE
					QueryOptionsHandle qoh = buildOptionsInCode();
					logger.info("write to db");				
					optionsMgr.writeOptions(OPTIONS_NAME, qoh);						
				}

				
				logger.info("wrote options to db");
				
				// create a handle to receive the query options
				StringHandle readHandle = new StringHandle();

				// read the query options from the database
				optionsMgr.readOptions(OPTIONS_NAME, readHandle);
				
				logger.info("Search Options" +" named "+ OPTIONS_NAME + " : \n" + readHandle.get());

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
	public static void testStructuredQuery() {
		try {

			MarkLogicConnection conn =  MarkLogicConnection.getInstance("data/marklogic.properties");
			
			try {
	
				QueryManager queryMgr = conn.getClient().newQueryManager();
				
				// define namespace on the server 
/*		        NamespacesManager nsMgr = conn.getClient().newServerConfigManager().newNamespacesManager();
		        
		        nsMgr.updatePrefix("ts", "http://marklogic.com/MLU/top-songs");
*/		        //nsMgr.deletePrefix("ts");

				// doesn't work?
				conn.getClient().newServerConfigManager().setServerRequestLogging(true);

				//set up the search argument
				String arg = "*Rainy*";
				
				//set up element to be searched
				String element = "title";
				
				// create a query builder for the query options
		        StructuredQueryBuilder qb = new StructuredQueryBuilder(null);

		        // build a search definition
		        StructuredQueryDefinition querydef
		                = qb.valueConstraint("ts:"+element, arg);

				// create a search definition
				// no options needed for this search (is not a search:search)
				//KeyValueQueryDefinition querydef = queryMgr.newKeyValueDefinition(null);
				
				
				//querydef.put(queryMgr.newElementLocator(new QName("ts:"+element)), arg);

				// create a handle for the search results
				SearchHandle resultsHandle = new SearchHandle();

				// run the search
				queryMgr.search(querydef, resultsHandle);

				logger.info("Matched "+resultsHandle.getTotalResults()+
						" documents in element '"+element+"' of/containing '"+arg+"'\n");
		
		
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
	private static QueryOptionsHandle buildOptionsInCode() {
		// Create a builder for constructing query configurations.
		QueryOptionsBuilder qob = new QueryOptionsBuilder();

		// create the query options
		QueryOptionsHandle queryOptions = new QueryOptionsHandle();
		
		// now build them
		//queryOptions.build();
				
		//RangeIndexType rit = RangeIndexType
		
/*		queryOptions.build(
				qob.constraint("title",
						qob.value(
								qob.elementRangeIndex("ts:title", null) )));
*/		// set debug
		queryOptions.setDebug(true);
		
		// write the query options to the database
		// optionsMgr.writeOptions(OPTIONS_NAME, queryOptions);

		QueryOptionsHandle handle = new QueryOptionsHandle();
		// see the query in the MarkLogic log
		handle.setDebug(true);
		QueryTransformResults transformResults = new QueryTransformResults();
		transformResults.setApply("raw");
		handle.setTransformResults( transformResults );
	
		return handle;
	}
	private static InputStreamHandle readOptionsFromFile() {
		InputStream optsStream = null;
		try {
			optsStream = LoadOptions.class.getClassLoader().getResourceAsStream(OPTIONS_FILENAME);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		if (optsStream == null)
			throw new RuntimeException("Could not read marklogic search options in " + OPTIONS_FILENAME);

		InputStreamHandle ish = new InputStreamHandle(optsStream);
		
	    return ish;
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testStructuredQuery();

	}
}
