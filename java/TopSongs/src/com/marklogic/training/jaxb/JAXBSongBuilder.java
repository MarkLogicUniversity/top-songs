package com.marklogic.training.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.training.MarkLogicConnection;
import com.marklogic.training.SongBuilder;
import com.marklogic.training.model.Song;

public class JAXBSongBuilder implements SongBuilder {

	private MarkLogicConnection conn = null;
	
	private static final Logger logger = LoggerFactory.getLogger(JAXBSongBuilder.class);

	public JAXBSongBuilder(MarkLogicConnection conn) {
		this.conn = conn;
	}

	@Override
	public void setConnection(MarkLogicConnection conn) {
		this.conn = conn;
	}
	
	@Override
	public Song getSongDetails(String uri) {
		
		Topsong topsong = getTopSong(uri);
		if (topsong == null) 
			return null;

		Song song = getSongInternal(uri, topsong);
				
		// now fill up all the additional details
		song.setAlbum( (topsong.getAlbum() == null?"": topsong.getAlbum().getAlbum() ) );
		if (topsong.getAlbum() == null) {
			song.setAlbum( "" );
			song.setAlbumimage("");
		} else {
			song.setAlbum(topsong.getAlbum().getAlbum() );
			song.setAlbumimage( (topsong.getAlbum().getUri() == null?"":topsong.getAlbum().getUri() ));			
		}
		logger.debug(" Song album " + song.getAlbum() );
		logger.debug(" Song album image uri " + song.getAlbumimage() );

		song.setLabel((topsong.getLabel() == null?"":topsong.getLabel() ) );
		logger.debug(" Song label " + song.getLabel() );
		
		song.setWriters((topsong.getWriters() == null?"":topsong.getWriters().toCSL() ) );
		logger.debug(" Song writers " + song.getWriters() );
		
		song.setProducers( (topsong.getProducers() == null?"": topsong.getProducers().toCSL() ) );
		logger.debug(" Song producers " + song.getProducers() );
		
		song.setFormats( (topsong.getFormats() == null?"":topsong.getFormats().toCSL() ) );
		logger.debug(" Song formats " + song.getFormats() );
		
		song.setLengths( (topsong.getLengths() == null?"":topsong.getLengths().toCSL()  ));
		logger.debug(" Song lengths " + song.getLengths() );
		
		song.setDescription(topsong.stringifyDescr() ); 
		logger.debug(" Song description " + song.getDescription() );

		song.setWeeks((topsong.getWeeks() == null?"": topsong.getWeeks().toCSL()) );
		logger.debug(" Song actual weeks at #1 " + song.getWeeks() );
		
		
		return song;
	}

	@Override
	public Song getSong(String uri) {
		Topsong topsong = getTopSong(uri);
		if (topsong == null) 
			return null;

		return getSongInternal(uri, topsong);
	}
	
	@Override
	public void insertSong(String title, String artist, String album, String genres, String writers, 
			String producers, String label, String description, String weekString) 
	{
		List<String> week = new ArrayList<String>();
		String[] weekTokens = weekString.split(",");
		logger.debug("found "+weekTokens.length + "weeks");
		for (String weekToken: weekTokens) {
			logger.debug("week is "+ weekToken);
			week.add(weekToken);
		}
		
		Weeks weeks = new Weeks();
		if (weekTokens.length == 0)
			weeks.setLast("");
		else
			weeks.setLast(weekTokens[weekTokens.length-1]);
		
		weeks.setWeek(week);
		
		
		String[] genreTokens = genres.split(",");
		List<String> gs = new ArrayList<String>();
		for (String genreToken: genreTokens) {
			gs.add(genreToken);
		}
		Genres g = new Genres(gs); 
		
		List<String> ps = new ArrayList<String>();
		String[] producerTokens = producers.split(",");
		for (String producerToken: producerTokens) 
			ps.add(producerToken);
		
		Producers p = new Producers(ps);
				
		List<String> ws = new ArrayList<String>();
		String[] writerTokens = writers.split(",");
		for (String writerToken: writerTokens) 
			ws.add(writerToken);		
		Writers w = new Writers(ws);
		
		Album a = new Album(album,"");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		Document doc = null;
		Element descr = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			Element d = doc.createElement("descr");
			d.appendChild(doc.createTextNode(description));
			descr = d;
		} catch (ParserConfigurationException e) {
			logger.error("caught exception trying to create DOM tree"+e.toString());
			
		}	
		
		
		Topsong ts = new Topsong();
		ts.setAlbum(a);
		ts.setArtist(artist);
		ts.setDescr(descr);
		ts.setFormats(null);
		ts.setGenres(g);
		ts.setLabel(label);
		ts.setLengths(null);
		ts.setProducers(p);
		ts.setWeeks(weeks);
		ts.setRecorded("");
		ts.setReleased("");
		ts.setTitle(title);
		ts.setWriters(w);
		
		// create a manager for XML documents
		XMLDocumentManager docMgr = conn.getClient().newXMLDocumentManager();	
		
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Topsong.class);
		} catch (JAXBException e) {
			logger.error("caught exception creating JAXB context"+e.toString());
		
		}

		JAXBHandle writeHandle = new JAXBHandle(context);
		String uri = ("/songs/"+artist+"+"+title+".xml").replaceAll(" ", "-");
		logger.debug("writing song " + uri+" to ML");
		writeHandle.set(ts);
		docMgr.write(uri, writeHandle);
		// read the persisted XML document for the logging message
		String songDoc = docMgr.read(uri, new StringHandle()).get();
		
		logger.debug("read document: \n"+songDoc);						

	}
	
	private Song getSongInternal(String uri,Topsong topsong) {
		Song song = new Song();
		// implement me !!
		song.setTitle((topsong.getTitle() == null?"":topsong.getTitle() ) );
		logger.debug("song title is " + song.getTitle() );

		
		song.setArtist((topsong.getArtist() == null?"":topsong.getArtist() ) );
		logger.debug("song artist is " + song.getArtist() );

		song.setUri((uri == null?"": uri));
		logger.debug("song uri is " + song.getPlainTextUri() );
		
		song.setGenres( (topsong.getGenres() == null?"": topsong.getGenres().toCSL() ) );
		logger.debug("song genres is/are " + song.getGenres() );
		 
		song.setWeekending((topsong.getWeeks() == null?"": topsong.getWeeks().getLast() ) );
		logger.debug("song last week in charts was " + song.getWeekending() );

		song.setTotalweeks((topsong.getWeeks() == null?0:topsong.getWeeks().getWeek().size() ) );
		logger.debug(" number of weeks at #1 was " + song.getTotalweeks() );
		
		return song;
	}
	
	private Topsong getTopSong(String uri) {
		// create a manager for XML documents
		XMLDocumentManager docMgr = conn.getClient().newXMLDocumentManager();	
		
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Topsong.class);
			// create a handle to receive the document content
			JAXBHandle readHandle = new JAXBHandle(context);

			// read the JAXB object from the document content
			docMgr.read(uri, readHandle);

			// access the document content
			return (Topsong) readHandle.get();

		} catch (JAXBException e) {
			logger.error(" caught exception " + e.toString() + " could not create JAXB Context for document "+uri);
			return null;
		}


	}
	

}
