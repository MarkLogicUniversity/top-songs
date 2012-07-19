package com.marklogic.training;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.document.XMLDocumentManager.DocumentRepair;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.StringHandle;

public class LoadDocumentMIMEandRepair {

	private static final Logger logger = LoggerFactory.getLogger(LoadDocumentMIMEandRepair.class);
	
	public static void load(String path, String filename) {
		try {
			MarkLogicConnection conn = new MarkLogicConnection();
			
			try {
				// create a manager for XML documents
				XMLDocumentManager docMgr = conn.getClient().newXMLDocumentManager();				
				// repair : fix missing tags during load
				docMgr.setDocumentRepair(DocumentRepair.FULL);
				logger.info("Document repair"+(docMgr.getDocumentRepair() == DocumentRepair.FULL?" set to FULL":" not set") );
				
				// read document from file 
				InputStream docStream = new BufferedInputStream(new FileInputStream(path + File.separator + filename) );
		
				// create a handle on the content
				InputStreamHandle contentHandle = new InputStreamHandle(docStream);
				/*
				String s = "<html>"+
						"<head>"+
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>"+
						"</head>"+
						"<body>"+
						"It's <b><i>very</b></i> interesting to see how music has changed over the years."+
						"</body>"+
						"</html>";

				String docId = "/testjava/repair.xhtml";
						
				docMgr.write(docId, new StringHandle(s));
				*/
				
				String uid = "/songs/"+filename;
				// create the handle for the document options
				DocumentMetadataHandle metadataHandle = new DocumentMetadataHandle();
				// set the document format
				metadataHandle.setFormat(Format.XML);
				
				logger.info("Writing document to db"  );
				
				// write to the db
				docMgr.write(uid,metadataHandle,contentHandle);

				logger.info("now read it back..."  );
				
				// retrieve the document - set up the handles
				// 1. content handle - use a StringHandle 
				StringHandle sh = new StringHandle();
				// 2. read the doc
				docMgr.read(uid, sh);
				//docMgr.read(docId, sh);
				
				// 3. log the document as a string
				logger.info("Document : note the fixed tags!!! \n"+sh.get() );
							    
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
		String pathToFile = "mls-developer/unit12";
		String fileName = "overtheyears.html";
		load(pathToExercises+File.separator+pathToFile, fileName);
	}

}
