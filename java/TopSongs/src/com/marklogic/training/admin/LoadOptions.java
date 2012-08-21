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

	/*
	 * 
	 * This class loads all the options necessary for the Top Songs project (actually the class LoadOptionsProgrammatically.java
	 * also loads another set of options (no overlap with this class) - this should be run too).
	 * 
	 * Important to note that when reading options from a file you can use any normal handle - there is no need to use the QueryOptionsHandle.
	 * Just need to make sure that the QueryOptionsManager is used to write it to the database.
	 */
	public static void load(boolean isSourceFile, String options, String optionsFilename) {
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
					InputStreamHandle ish = readOptionsFromFile(optionsFilename);
					logger.info("write following options to db :\n"+ish.toString());				
					optionsMgr.writeOptions(options, ish);	

				} else {
					// build options - FROM CODE
					QueryOptionsHandle qoh = buildOptionsInCode();
					logger.info("write to db");				
					optionsMgr.writeOptions(options, qoh);						
				}

				
				logger.info("wrote options to db");
				
				// create a handle to receive the query options
				StringHandle readHandle = new StringHandle();

				// read the query options from the database
				optionsMgr.readOptions(options, readHandle);
				
				logger.info("Search Options" +" named "+ options + " : \n" + readHandle.get());

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
	private static InputStreamHandle readOptionsFromFile(String filename) {
		InputStream optsStream = null;
		try {
			optsStream = LoadOptions.class.getClassLoader().getResourceAsStream(filename);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		if (optsStream == null)
			throw new RuntimeException("Could not read marklogic search options in " + filename);

		InputStreamHandle ish = new InputStreamHandle(optsStream);
		
	    return ish;
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		load(true,"genre-options"			,"data/query-options-genre.xml");
		load(true,"full-options"			,"data/query-options-full.xml");
		load(true,"facets-only-full-options","data/query-options-facets-only.xml");
		load(true,"suggestion-options"		,"data/suggestions-options.xml");

	}

}
