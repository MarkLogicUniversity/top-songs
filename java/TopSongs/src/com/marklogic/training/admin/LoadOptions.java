package com.marklogic.training.admin;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.admin.config.QueryOptions.QueryTransformResults;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.QueryOptionsHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.training.MarkLogicConnection;

public class LoadOptions {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadOptions.class);
	private static final String OPTIONS_NAME = "genre-options";
	private static final String OPTIONS_FILENAME = "data/query-options-genre.xml";
//	private static final String OPTIONS_NAME = "full-options";
//	private static final String OPTIONS_FILENAME = "data/query-options-full.xml";
//	private static final String OPTIONS_NAME = "facets-only-full-options";
//	private static final String OPTIONS_FILENAME = "data/query-options-facets-only.xml";	
//	private static final String OPTIONS_NAME = "suggestion-options";
//	private static final String OPTIONS_FILENAME = "data/suggestions-options.xml";	

	/*
	 * Important to note that when reading options from a file you can use any normal handle - there is no need to use the QueryOptionsHandle.
	 * Just need to make sure that the QueryOptionsManager is used to write it to the database.
	 */
	public static void load(boolean isSourceFile) {
		try {
			// we are using admin-role credentials in order to write new query options
			// (note the properties filename)
			MarkLogicConnection conn =  new MarkLogicConnection("data/marklogic-admin.properties");
			
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
	private static QueryOptionsHandle buildOptionsInCode() {
		// create some basic options
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
		load(true);

	}

}
