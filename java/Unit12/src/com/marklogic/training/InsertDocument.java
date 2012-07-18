package com.marklogic.training;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.marklogic.client.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.StringHandle;

public class InsertDocument {

	private static final Logger logger = LoggerFactory.getLogger(LoadDocument.class);
	
	/**
	 * create the following Document in memory using DOM and write it to the database
	 * 
	 * <top-song>
	 * 	<title>Maney for Nothing</title>
	 * 	<artist>Dire Straits</artist>
	 * 	<weeks>
	 * 		<week>1985-09-21</week>
	 * 		<week>1985-09-28</week>
	 * 		<week>1985-10-05</week>
	 * 	</weeks>
	 * 	<album>Brothers in Arms</album>
	 * </top-song>
	 * 
	 * 
	 */
	public static void insert() {
		try {
			MarkLogicConnection conn = new MarkLogicConnection();
			
			try {

				// create a manager for XML documents
				XMLDocumentManager docMgr = conn.getClient().newXMLDocumentManager();	
				// create DOM Handle for writing the document to the db
				DOMHandle handle = new DOMHandle();
				
				// create an XML doc builder factory using the DOM handle
				DocumentBuilderFactory docFactory = handle.getFactory();
				
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		 
				// root element
				org.w3c.dom.Document doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("top-song");
				doc.appendChild(rootElement);
		 
				// title elements
				Element title = doc.createElement("title");
				title.appendChild(doc.createTextNode("Money for Nothing"));
				rootElement.appendChild(title);
		 		 
				// artist elements
				Element artist = doc.createElement("artist");
				artist.appendChild(doc.createTextNode("Dire Straits"));
				rootElement.appendChild(artist);
		 
				// weeks elements
				Element weeks = doc.createElement("weeks");
				// week element
				Element week1 = doc.createElement("week");
				week1.appendChild(doc.createTextNode("1985-09-21"));	
				weeks.appendChild(week1);
				// week element
				Element week2 = doc.createElement("week");
				week2.appendChild(doc.createTextNode("1985-09-28"));	
				weeks.appendChild(week2);
				// week element
				Element week3 = doc.createElement("week");
				week3.appendChild(doc.createTextNode("1985-10-05"));	
				weeks.appendChild(week3);
				rootElement.appendChild(weeks);
									 
				// album element
				Element album = doc.createElement("album");
				album.appendChild(doc.createTextNode("Brothers in Arms"));
				rootElement.appendChild(album);
		 				
				// store the doc in the DOM handle
				handle.set(doc);
				
				// set URI
				String uri = "song1.xml";
				// write the document to the database
				docMgr.write(uri, handle);
				logger.info("wrote document to db");
				
				logger.info("now read it back..."  );
				
				// retrieve the document - set up the handles
				// 1. content handle - use a StringHandle 
				StringHandle sh = new StringHandle();
				// 2. read the doc
				docMgr.read(uri, sh);
				// 3. log the document as a string
				logger.info("Document inserted is : \n"+sh.get() );
				
				
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
		// run insert
		insert();

	}

}
