package com.marklogic.training;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SongHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(SongHelper.class);

	public static String getTagValue(String tagName,Document doc) {
		
		NodeList nodeList = doc.getElementsByTagName(tagName);
		
		if (nodeList == null)
			return "";

		if (nodeList.getLength() == 0)
			return "";

		logger.debug("found "+nodeList.getLength() + " elements "); 
		
        Node node = nodeList.item(0);

		logger.debug("found node : "+ node.getNodeName() + " of type " + (node.getNodeType() == Node.ELEMENT_NODE? " element ":" sommat else") + " with value " + node.getNodeValue()); 

		NodeList childList = node.getChildNodes();

		logger.debug("found "+childList.getLength() + " child elements of "+ node.getNodeName() ); 

		Node text = childList.item(0);
		
		logger.debug("found node : "+ text.getNodeName() + " of type " + (text.getNodeType() == Node.TEXT_NODE ? " text node ":" sommat else") + " with value " + text.getNodeValue()); 
        
        return text.getNodeValue();
		
	}

	public static String get2ndLevelChildren(String tagName,Document doc) {
		// get genres
		// get each child node (genre)
		// for each genre get each child text node , add to list (in lower case)
		// join the list with comma separators, return
		
		NodeList nodeList = doc.getElementsByTagName(tagName);
		
		logger.debug("found "+nodeList.getLength() + " elements "); 
		
		// return if there are no genres
		if (nodeList.getLength() == 0 )
			return "";
		
        Node node = nodeList.item(0);

		logger.debug("found node : "+ node.getNodeName() + " of type " + (node.getNodeType() == Node.ELEMENT_NODE? " element ":" sommat else") + " with value " + node.getNodeValue()); 
		
		NodeList childList = node.getChildNodes();

		logger.debug("found "+childList.getLength() + " child elements of "+ node.getNodeName() ); 
		
	    StringBuilder sb = new StringBuilder();
		
	    int childCount = 0;
		
		for (int i=0; i<childList.getLength(); i++) {
			
			   Node n = childList.item(i);
			   if (n.getNodeType() == Node.ELEMENT_NODE) {
				   
				  // comma separated list
				  if (childCount > 0)
					  sb.append(", ");

				  childCount++;
				   
			      Element el = (Element) n;
			      
			      String childValue = el.getChildNodes().item(0).getNodeValue();
			      logger.debug("found the following genre " + childValue);
			      sb.append(childValue );
			      
			   }
		}
		
		return sb.toString();
	}

	
	public static String getGenres(Document doc) {
		// get genres
		// get each child node (genre)
		// for each genre get each child text node , add to list (in lower case)
		// join the list with comma separators, return
		
		NodeList nodeList = doc.getElementsByTagName("genres");
		
		logger.debug("found "+nodeList.getLength() + " elements "); 
		
		// return if there are no genres
		if (nodeList.getLength() == 0 )
			return "";
		
        Node node = nodeList.item(0);

		logger.debug("found node : "+ node.getNodeName() + " of type " + (node.getNodeType() == Node.ELEMENT_NODE? " element ":" sommat else") + " with value " + node.getNodeValue()); 
		
		NodeList childList = node.getChildNodes();

		logger.debug("found "+childList.getLength() + " child elements of "+ node.getNodeName() ); 
		
	    StringBuilder sb = new StringBuilder();
		
	    int genreCount = 0;
		
		for (int i=0; i<childList.getLength(); i++) {
			
			   Node n = childList.item(i);
			   if (n.getNodeType() == Node.ELEMENT_NODE) {
				   
				  // comma separated list
				  if (genreCount > 0)
					  sb.append(",");

				  genreCount++;
				   
			      Element el = (Element) n;
			      
			      String genre = el.getChildNodes().item(0).getNodeValue();
			      logger.debug("found the following genre " + genre);
			      sb.append(genre.toLowerCase() );
			      
			   }
		}
		
		return sb.toString();
	}
	
	public static String getWeekLastAttr(Document doc) {
		
		Element weeks = getWeeksNode(doc);
		
		if (weeks == null)
			return "";
		Attr last = weeks.getAttributeNode("last");
		if (last == null)
			return "";
		String lastValue = last.getChildNodes().item(0).getNodeValue();
		if (lastValue == null)
			lastValue = "";
		
		return lastValue;
	}
	
	public static long getNumberOfWeeks(Document doc) {
		Element weeks = getWeeksNode(doc);
		if (weeks == null)
			return 0;
		NodeList nl = weeks.getElementsByTagName("week");
		if (nl == null)
			return 0;
		if (nl.getLength() == 0) 
			return 0;		
		return nl.getLength();
	}
	
	private static Element getWeeksNode(Document doc) {

		NodeList nodeList = doc.getElementsByTagName("weeks");
		
		logger.debug("found "+nodeList.getLength() + " elements "); 
		
		// return if there are no genres
		if (nodeList.getLength() == 0 )
			return null;
		
		if (nodeList.item(0).getNodeType() == Node.ELEMENT_NODE ) {
			return (Element) nodeList.item(0);
		} else {
			return null;
		}
		
	}
	public static String getSongDescription(Document doc,long numberOfWords) {

		NodeList nodeList = doc.getElementsByTagName("descr");
		
		logger.debug("found "+nodeList.getLength() + " elements "); 
		
		// return if there are no genres
		if (nodeList.getLength() == 0 )
			return "";
		
		Element descrNode = (Element) nodeList.item(0);

	    String xmlString = null;
	    try
	    {
	      // Set up the output transformer
	      TransformerFactory transfac = TransformerFactory.newInstance();
	      Transformer trans = transfac.newTransformer();
	      trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	      trans.setOutputProperty(OutputKeys.INDENT, "no");

	      // Print the DOM node

	      StringWriter sw = new StringWriter();
	      StreamResult result = new StreamResult(sw);
	      DOMSource source = new DOMSource(descrNode);
	      trans.transform(source, result);
	      xmlString = sw.toString();
	      logger.debug(" read description as XMLstring "+xmlString);
	     
	      
	    }
	    catch (TransformerException e)
	    {
	      logger.error("caught exception while transforming DOM description node " + e.toString());
	      xmlString = "description not found";
	    }
	    
	    String[] shortenedDesc = xmlString.split(" ");
	    StringBuilder result = new StringBuilder();
	    long len = (numberOfWords == 99999 ? shortenedDesc.length : numberOfWords);
	    for (int i=0; i < len; i++) {
	    	result.append(shortenedDesc[i]);
	    	result.append(" ");
	    }
		return result.toString();
	}
	public static String getAlbumURI(Document doc) {
		
		NodeList nodeList = doc.getElementsByTagName("album");
		
		logger.debug("found "+nodeList.getLength() + " elements "); 
		
		// return if there are no genres
		if (nodeList.getLength() == 0 )
			return "";
		
        Element album = (Element) nodeList.item(0);
        if (album == null)
        	return "";
        
        Attr uriNode = album.getAttributeNode("uri");
        
        if (uriNode == null) 
        	return "";
        
		String uri = uriNode.getChildNodes().item(0).getNodeValue();
		
		if (uri == null)
			uri = "";

 		return uri;
		
	}

}
