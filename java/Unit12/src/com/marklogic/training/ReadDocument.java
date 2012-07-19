package com.marklogic.training;

import java.io.File;
import java.io.StringWriter;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.StringHandle;

import com.marklogic.client.example.handle.JDOMHandle;


public class ReadDocument {
	
	private static final Logger logger = LoggerFactory.getLogger(ReadDocument.class);
	
	/**
	 * read a document from the database into a different data sources (Handles)
	 * @param uid
	 */
	public static void read(String uid) {
		try {
			MarkLogicConnection conn = new MarkLogicConnection();
			
			try {				
				// create a manager for XML documents
				XMLDocumentManager docMgr = conn.getClient().newXMLDocumentManager();				
				
				// Read Document into a String
				// create String handle
				StringHandle strHandle = new StringHandle();  
				// read document
				docMgr.read(uid,strHandle);
				// output the string
				logger.info("Document from String \n" + strHandle.get() );
				
				// Read Document into a DOM Tree for processing
				// create BytesHandles
				DOMHandle domHandler = new DOMHandle();
				// read doc into DOM Tree
				docMgr.read(uid, domHandler);

				// get a DOM document
				Document doc = domHandler.get();
				// get root element from DOM
				String rootName = doc.getDocumentElement().getTagName();
				logger.info("Document from DOM tree content with the <"+rootName+"/> root element");
				
				
				// Read Document into JDOM tree
				JDOMHandle jh = new JDOMHandle();
				// read doc into JDOM Tree
				docMgr.read(uid, jh);
				// get the JDOM doc
				org.jdom2.Document doc2 = jh.get();
				// set up the formatter
				XMLOutputter xo = new XMLOutputter(Format.getPrettyFormat()); 
				// create a new string data sink
				StringWriter sw = new StringWriter();
				// write to the String Writer
				xo.output(doc2,sw);
				logger.info("JDOM Document tree /n"+sw.toString());
				sw.close();		
				
			    
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
		read(pathToExercises+File.separator+pathToFile+File.separator+fileName);
	}

}
