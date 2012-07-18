package com.marklogic.training;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.Format;
import com.marklogic.client.XMLDocumentManager;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.DocumentMetadataHandle.DocumentCollections;
import com.marklogic.client.io.DocumentMetadataHandle.DocumentPermissions;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.ReaderHandle;

public class LoadDocumentWithOptions {

	private static final Logger logger = LoggerFactory.getLogger(LoadDocumentWithOptions.class);
	
	public static void load(String path, String filename) {
		try {
			MarkLogicConnection conn = new MarkLogicConnection();
			
			try {
				// create a manager for XML documents
				XMLDocumentManager docMgr = conn.getClient().newXMLDocumentManager();				

				// read document from file 
				InputStream docStream = new BufferedInputStream(new FileInputStream(path + File.separator + filename) );
		
				// create a handle on the content
				InputStreamHandle contentHandle = new InputStreamHandle(docStream);
				
				String uid = "/songs/"+filename;
				// create the handle for the document options
				DocumentMetadataHandle metadataHandle = new DocumentMetadataHandle();
				// set the collections to which the document should belong 
				// (getCollections() is a factory for DocumentCollections as well as an accessor)
				metadataHandle.getCollections().addAll();
				// Normally the second and subsequent parameters would be capabilities like Capability.UPDATE..
				// Leave these blank to pick up the default permissions defined for the role
				metadataHandle.getPermissions().add("rest-writer");
				// set the document format
				metadataHandle.setFormat(Format.XML);
				
				logger.info("Writing document to db"  );
				
				// write to the db
				docMgr.write(uid,metadataHandle,contentHandle);

				logger.info("now read it back..."  );
				
				// retrieve the document - set up the handles
				// 1. metadata handle
				DocumentMetadataHandle mdhRead = new DocumentMetadataHandle();
				// 2. content handle - use a ReaderHandle in this case 
				//    ( a StringHandle would be more easier and also more appropriate as we want to log it)
				ReaderHandle rh = new ReaderHandle();
				// 3. read the doc
				docMgr.read(uid, mdhRead, rh);
				// 4. grab the Reader and make it buffered
				BufferedReader br = new BufferedReader(rh.get());
				// 5. now write the document to the log (dontcha love Java IO)
				String s = new String();
				try {
					while ((s = br.readLine()) != null)
							logger.info(s);
				} catch (Exception e) {
					// swallow this
				} finally {
					if (br != null)
						br.close();
				}
				
				// 6. now write out the properties from the metadata handle
				DocumentCollections collections = mdhRead.getCollections();
				logger.info("Document belongs to the following collections "+collections.toString());
				DocumentPermissions permissions = mdhRead.getPermissions();
				logger.info("Document has the following permissions "+permissions.toString());
				logger.info("Document has "+(mdhRead.getFormat() == Format.XML?"XML format":"non-XML format") );
							    
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
		String fileName = "The-Doors+Light-My-Fire.xml";
		load(pathToExercises+File.separator+pathToFile, fileName);

	}

}
