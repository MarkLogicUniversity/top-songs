package com.marklogic.training;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.training.jaxb.Album;
import com.marklogic.training.jaxb.Formats;
import com.marklogic.training.jaxb.Genres;
import com.marklogic.training.jaxb.Lengths;
import com.marklogic.training.jaxb.Producers;
import com.marklogic.training.jaxb.Topsong;
import com.marklogic.training.jaxb.Weeks;
import com.marklogic.training.jaxb.Writers;

public class TestJAXB {

	private static final Logger logger = LoggerFactory.getLogger(TestJAXB.class);
	
	public static Topsong createSong() {
		
		List<String> week = new ArrayList<String>();
		week.add("1977-04-30");
		week.add("1977-04-21");
		week.add("1977-04-14");
		Weeks weeks = new Weeks();
		weeks.setLast("1977-04-30");
		weeks.setWeek(week);
		
		List<String> fs = new ArrayList<String>();
		fs.add("7\"");
		Formats f = new Formats(fs);
		
		List<String> gs = new ArrayList<String>();
		gs.add("rock");
		gs.add("country");
		Genres g = new Genres(gs); 

		
		List<String> ls = new ArrayList<String>();
		ls.add("3:00");
		Lengths l = new Lengths(ls);
		
		List<String> ps = new ArrayList<String>();
		ps.add("Gary Klein");
		Producers p = new Producers(ps);
				
		List<String> ws = new ArrayList<String>();
		ws.add("Allen Toussaint");
		Writers w = new Writers(ws);
		
		Album a = new Album("Southern Nights","/images/50-Cent-featuring-Olivia+Candy-Shop.jpg");
		
		Topsong ts = new Topsong();
		ts.setAlbum(a);
		ts.setArtist("Glen Cambell");
		//ts.setDescr("<p>\"Southern Nights\" is a song written by Allen Toussaint and most famously recorded by country-pop singer Glen Campbell. Thesong enjoyed immense popularity with both country and popaudiences when Campbell covered it in 1977.</p><p>Campbell had been a leader in crossover music during the 1960s and 1970s, and continued that trend when he recorded Toussaint\'s \"Southern Nights.\" Toussaint recalled his memories");
		ts.setFormats(f);
		ts.setGenres(g);
		ts.setLabel("Capitol Records 4376");
		ts.setLengths(l);
		ts.setProducers(p);
		ts.setWeeks(weeks);
		ts.setRecorded("1976");
		ts.setReleased("January 1977 ( U.S. )");
		ts.setTitle("Southern Nights");
		ts.setWriters(w);
		
		return ts;
	}
	public static void load() {
		try {
			MarkLogicConnection conn = MarkLogicConnection.getInstance();

			
			try {

				// create a manager for XML documents
				XMLDocumentManager docMgr = conn.getClient().newXMLDocumentManager();	
				
				// create an identifier for the document
				String docId = "/songs/Glen-Campbell+Southern-Nights.xml";
				
				JAXBContext context = JAXBContext.newInstance(Topsong.class);

				// create a handle to receive the document content
				JAXBHandle readHandle = new JAXBHandle(context);

				// read the JAXB object from the document content
				docMgr.read(docId, readHandle);

				// access the document content
				Topsong song = (Topsong) readHandle.get();
				  // something new 
				// ... do something with the JAXB object ...
			    
				
				logger.info("read top song from DB "+song.toString() );
				
				// read the persisted XML document for the logging message
				//String songDoc = docMgr.read(docId, new StringHandle()).get();
				
				//logger.info("read document: \n"+songDoc);
						
/*				Topsong song1 = createSong();
				JAXBHandle writeHandle = new JAXBHandle(context);
				String uri = "/songs/arse";
				writeHandle.set(song1);
				docMgr.write(uri, writeHandle);
				// read the persisted XML document for the logging message
				String songDoc = docMgr.read(uri, new StringHandle()).get();
				
				logger.info("read document: \n"+songDoc);						
*/			
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
		load();
	
		
	}

}
