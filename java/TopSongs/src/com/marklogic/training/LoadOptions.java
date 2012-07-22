package com.marklogic.training;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.admin.config.QueryOptions.QueryTransformResults;
import com.marklogic.client.io.QueryOptionsHandle;

public class LoadOptions {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadOptions.class);
	//private static final String OPTIONS_NAME = "full-options";
	//private static final String OPTIONS_NAME = "full-options";
	private static final String OPTIONS_NAME = "advanced-options";
	private static final String OPTIONS_FILENAME = "data/query-options-genre.xml";
	private static final int BUFFER_SIZE = 32*1024;
	

	public static void load(boolean isSourceFile) {
		try {
			MarkLogicConnection conn =  MarkLogicConnection.getInstance("data/marklogic-admin.properties");
			
			try {
				
				// need admin authority to perform this 	 					 
				// create a manager for writing query options
				QueryOptionsManager optionsMgr = conn.getClient().newServerConfigManager().newQueryOptionsManager();

				QueryOptionsHandle handle = null;
				if (isSourceFile) {
				    // build options - FROM FILE
					logger.info("set handle from byte[]");
					handle = readOptionsFromFile();
				} else {
					// build options - FROM CODE
					 handle = buildOptionsInCode();		
				}

				logger.info("write to db");				
				// write them to the db
				optionsMgr.writeOptions(OPTIONS_NAME, handle);				
				
				logger.info("wrote options to db");
				
				// create a handle to receive the query options
				QueryOptionsHandle readHandle = new QueryOptionsHandle();

				// read the query options from the database
				optionsMgr.readOptions(OPTIONS_NAME, readHandle);
				
				logger.info("Search Options" +" named "+ OPTIONS_NAME + " : \n" + readHandle.toXMLString());

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
	private static QueryOptionsHandle readOptionsFromFile() {
		InputStream optsStream = null;
		try {
			optsStream = LoadOptions.class.getClassLoader().getResourceAsStream(OPTIONS_FILENAME);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		if (optsStream == null)
			throw new RuntimeException("Could not read marklogic search options in " + OPTIONS_FILENAME);
		
	    int len;
	    //HATEME fixed size, nasty, set to 32K though, that's funny right there.
	    // there's got to be an easier way to do this.
	    int size = BUFFER_SIZE;
	    byte[] buf;

	    try {
			if (optsStream instanceof ByteArrayInputStream) {
				logger.info("stream is a ByteArrayInputStream");
			    size = optsStream.available();
			    buf = new byte[size];
			    len = optsStream.read(buf, 0, size);
			} else {
				logger.info("stream is NOT a ByteArrayInputStream");
			    ByteArrayOutputStream bos = new ByteArrayOutputStream();
			    buf = new byte[size];
			    while ((len = optsStream.read(buf, 0, size)) != -1)
			    	bos.write(buf, 0, len);
			    buf = bos.toByteArray();
			}
		} catch (IOException e) {
			logger.error("failed to read options from file - caught exception "+e.toString());
			throw new RuntimeException(e);
		}
	    QueryOptionsHandle qoh = new QueryOptionsHandle();
	    qoh.fromBuffer(buf);
	    return qoh;
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		load(true);

	}

}
