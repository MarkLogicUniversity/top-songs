package com.marklogic.training;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.marklogic.client.XMLDocumentManager;

public class DeleteDocument {

	private static final Logger logger = LoggerFactory.getLogger(DeleteDocument.class);
	
	public static void delete(String uid) {
		try {
			MarkLogicConnection conn = new MarkLogicConnection();
			
			try {
				
				// create a manager for XML documents
				XMLDocumentManager docMgr = conn.getClient().newXMLDocumentManager();				
				
				logger.info("Delete Document " + uid);
				// delete the document by uid
				docMgr.delete(uid);
							    
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
		//set up document id to be read from the database
		String pathToExercises = "/Users/jcrean/Training/DevelopingMarkLogicApplications/Exercises";
		String pathToFile = "mls-developer/unit12/top-songs-source/songs";
		String fileName = "Coldplay+Viva-la-Vida.xml";
		delete(pathToExercises+File.separator+pathToFile+File.separator+fileName);

	}

}
