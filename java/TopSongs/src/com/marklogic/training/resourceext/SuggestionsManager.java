package com.marklogic.training.resourceext;

import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.extensions.ResourceManager;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.io.XMLStreamReaderHandle;
import com.marklogic.client.util.RequestParameters;
import com.marklogic.training.MarkLogicConnection;

public class SuggestionsManager extends ResourceManager {
	
	private static final Logger logger = LoggerFactory.getLogger(SuggestionsManager.class);
	
	static final public String NAME = "suggestions";
	
	private XMLDocumentManager docMgr;

	/*
	 * the suggest method here should be called to invoke search:suggest()
	 */
	public SuggestionsManager(MarkLogicConnection conn) {
		super();
		conn.getClient().init(NAME, this);
	}
	public String[] suggestArray(String q) {
		try {
			logger.info(" suggest called with q = "+q);
			RequestParameters params = new RequestParameters();
			params.add("service", "suggestions");
			params.add("q", q);

			XMLStreamReaderHandle readHandle = new XMLStreamReaderHandle();

			// call the service
			if (getServices() == null) {
				logger.error("getServices() returned null");
				return null;
			} else {
				getServices().get(params, readHandle);
			}

			XMLStreamReader streamReader = readHandle.get();
			
			QName wordName = new QName("", "suggestion");

			ArrayList<String> words  = new ArrayList<String>();

			while (streamReader.hasNext()) {
				int current = streamReader.next();
				if (current == XMLStreamReader.START_ELEMENT) {
					if (wordName.equals(streamReader.getName())) {
						words.add(streamReader.getElementText());
					}
				}
			}
			logger.info("received "+words.size()+" suggestions");
			return words.toArray(new String[words.size()]);
			
		} catch(XMLStreamException ex) {
			throw new RuntimeException(ex);
		}
		
	}
	public String suggest(String q) {
		try {
			logger.info(" suggest called with q = "+q);
			RequestParameters params = new RequestParameters();
			params.add("service", "suggestions");
			params.add("q", q);

			//XMLStreamReaderHandle readHandle = new XMLStreamReaderHandle();
			StringHandle readHandle = new StringHandle();
			// call the service
			if (getServices() == null) {
				logger.error("getServices() returned null");
				return null;
			} else {
				getServices().get(params, readHandle);
			}

			return readHandle.toString();
			
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MarkLogicConnection conn =  new MarkLogicConnection("data/marklogic.properties");
			
			try {
				SuggestionsManager sm = new SuggestionsManager(conn );
				
				logger.info(" calling suggest()..... "  );
				logger.info(" suggest string " + sm.suggest("*night*") ); 
					
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

}
