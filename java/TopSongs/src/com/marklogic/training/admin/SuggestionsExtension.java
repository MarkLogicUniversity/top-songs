package com.marklogic.training.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.admin.ExtensionMetadata;
import com.marklogic.client.admin.MethodType;
import com.marklogic.client.admin.ResourceExtensionsManager;
import com.marklogic.client.admin.ResourceExtensionsManager.MethodParameters;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.training.MarkLogicConnection;
import com.marklogic.training.resourceext.SuggestionsManager;
import com.marklogic.training.test.TestStructuredQuery;
/*
 * run the static method in this class ( in main() below) in order to define the extension to the REST api which will implement search:suggest()
 */
public class SuggestionsExtension {
	
	private static final Logger logger = LoggerFactory.getLogger(SuggestionsExtension.class);

	private static final String OPTIONS_NAME = "suggestion-options";
	private static final String OPTIONS_FILENAME = "data/suggestions-options.xml";	
	
	public static void installResourceExtension() {

		try {
			// we are using admin-role credentials in order to write new query options
			// (note the properties filename)
			MarkLogicConnection conn =  new MarkLogicConnection("data/marklogic-admin.properties");
			
			try {
		
				// create a manager for resource extensions
				ResourceExtensionsManager resourceMgr = conn.getClient().newServerConfigManager().newResourceExtensionsManager();
		
				// specify metadata about the resource extension
				ExtensionMetadata metadata = new ExtensionMetadata();
				metadata.setTitle("Search Suggestions");
				metadata.setDescription("implement Search Suggestions over REST");
				metadata.setProvider("MarkLogic University");
				metadata.setVersion("0.1");
		
				// acquire the resource extension source code
				InputStream sourceStream = SuggestionsExtension.class.getClassLoader().getResourceAsStream("extensions"+File.separator+"suggest.xqy");
				if (sourceStream == null)
					throw new IOException("Could not read example resource extension");
		
				// create a handle on the extension source code
				InputStreamHandle handle = new InputStreamHandle();
				handle.set(sourceStream);
		
				// write the resource extension to the database
				resourceMgr.writeServices(SuggestionsManager.NAME, handle, metadata, new MethodParameters(MethodType.GET));
		
				logger.info("Installed the resource extension on the server");

		
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		installResourceExtension();
	}

}
