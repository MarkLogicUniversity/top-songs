package com.marklogic.training;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.document.BinaryDocumentManager;
import com.marklogic.client.io.Format;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.io.BytesHandle;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.StringHandle;

public class LoadJSONandBinaryDocuments {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadDocumentMIMEandRepair.class);
	/**
	 * Write a JSON document into the db - it is stored in XML format.
	 * Retrieve it in JSON (default) or in another format by setting the format in the handle (e.g. XML)
	 * 
	 */
	public static void loadJSON() {
		try {
			MarkLogicConnection conn = new MarkLogicConnection();
			
			try {
				// create a manager for JSON documents
				JSONDocumentManager docMgr = conn.getClient().newJSONDocumentManager();		
				
				String filename = "data/flipper.json";

				InputStream docStream = LoadJSONandBinaryDocuments.class.getClassLoader().getResourceAsStream(filename);
						                                                    
				if (docStream == null)
					throw new RuntimeException("Could not read document " + filename);
			
				// create a handle on the content
				InputStreamHandle contentHandle = new InputStreamHandle(docStream);
								
				String uri = "/dolphins/"+filename;
				
				logger.info("Writing document to db"  );
				
				// write to the db
				docMgr.write(uri,contentHandle);

				logger.info("now read it back...in JSON... "  );
				
				// retrieve the document - set up the handles
				// 1. content handle - use a StringHandle 
				StringHandle sh = new StringHandle();
				// 2. read the doc
				docMgr.read(uri, sh);
				// 3. log the document as a string - it is 
				logger.info("Document in JSON : \n"+sh.get() );
				
				// now read the document in XML format
				DocumentMetadataHandle dmh = new DocumentMetadataHandle();
				dmh.setFormat(Format.XML);
				StringHandle shXML = new StringHandle();
				docMgr.read(uri,dmh, shXML);
				logger.info("Document in XML :\n "+shXML.get());
										    
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
	 * Write a JSON document into the db - it is stored in XML format.
	 * Retrieve it in JSON (default) or in another format by setting the format in the handle (e.g. XML)
	 * 
	 */
	public static void loadBinary() {
		try {
			MarkLogicConnection conn = new MarkLogicConnection();
			
			try {
				// create a manager for JSON documents
				BinaryDocumentManager docMgr = conn.getClient().newBinaryDocumentManager();		
				
				String filename = "data/mlfavicon.png";
				 
				InputStream docStream = LoadJSONandBinaryDocuments.class.getClassLoader().getResourceAsStream(filename);
						                                                    
				if (docStream == null)
					throw new RuntimeException("Could not read document " + filename);
			
				// create a handle on the content
				InputStreamHandle contentHandle = new InputStreamHandle(docStream);
								
				String uri = "/dolphins/"+filename;
				
				logger.info("Writing document to db"  );
				
				// write to the db
				docMgr.write(uri,contentHandle);

				logger.info("now read it back.... "  );
				
				// retrieve the document - set up the handles
				// 1. content handle - use a BytesHandle
				BytesHandle bh = new BytesHandle();
				// 2. read the doc
				docMgr.read(uri, bh);
				// 3. use the bytes..
				byte[] bytes = bh.get();
				
				logger.info("Document contains "+bytes.length+" bytes" );
											    
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
		loadJSON();
		loadBinary();

	}

}
