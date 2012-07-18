package com.marklogic.training;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.XMLDocumentManager;
import com.marklogic.client.io.InputStreamHandle;

public class LoadDocument {

	private static final Logger logger = LoggerFactory.getLogger(LoadDocument.class);
	

	public static void load(String filename) {
		try {
			MarkLogicConnection conn = new MarkLogicConnection();
			
			try {

				// create a manager for XML documents
				XMLDocumentManager docMgr = conn.getClient().newXMLDocumentManager();				
				
				// read document from file 
				InputStream docStream = new BufferedInputStream(new FileInputStream(filename) );
		
				// create a handle on the content
				InputStreamHandle handle = new InputStreamHandle(docStream);
	
				// write the document to the database
				// URI is set to the path of the document
				docMgr.write(filename, handle);
				logger.info("wrote document to db");
				
				
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
		// create an identifier for the document
		String pathToExercises = "/Users/jcrean/Training/DevelopingMarkLogicApplications/Exercises";
		String pathToFile = "mls-developer/unit12/top-songs-source/songs";
		String fileName = "Coldplay+Viva-la-Vida.xml";
		load(pathToExercises+File.separator+pathToFile+File.separator+fileName);

	}

}
