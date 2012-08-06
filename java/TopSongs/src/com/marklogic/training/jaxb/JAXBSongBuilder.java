package com.marklogic.training.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.JAXBHandle;
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
	public Song getSongDetails(String uri) {
		
		Topsong topsong = getTopSong(uri);
		if (topsong == null) 
			return null;

		Song song = getSongInternal(uri, topsong);
		// now fill up all the additional details
		song.setAlbum(topsong.getAlbum().getAlbum() );
		logger.debug(" Song album " + song.getAlbum() );
		
		song.setLabel(topsong.getLabel() );
		logger.debug(" Song label " + song.getLabel() );
		
		song.setWriters(topsong.getWriters().toCSL() );
		logger.debug(" Song writers " + song.getWriters() );
		
		song.setProducers(topsong.getProducers().toCSL() );
		logger.debug(" Song producers " + song.getProducers() );
		
		song.setFormats(topsong.getFormats().toCSL() );
		logger.debug(" Song formats " + song.getFormats() );
		
		song.setLengths(topsong.getLengths().toCSL() );
		logger.debug(" Song lengths " + song.getLengths() );
		
		song.setDescription(topsong.stringifyDescr() ); 
		logger.debug(" Song description " + song.getDescription() );

		song.setWeeks(topsong.getWeeks().toCSL() );
		logger.debug(" Song actual weeks at #1 " + song.getWeeks() );
		
		song.setAlbumimage(topsong.getAlbum().getUri());
		logger.debug(" Song album image uri " + song.getAlbumimage() );
		
		return song;
	}

	@Override
	public Song getSong(String uri) {
		Topsong topsong = getTopSong(uri);
		if (topsong == null) 
			return null;

		return getSongInternal(uri, topsong);
	}
	
	private Song getSongInternal(String uri,Topsong topsong) {
		Song song = new Song();
		// implement me !!
		song.setTitle(topsong.getTitle() );
		logger.debug("song title is " + song.getTitle() );

		song.setArtist(topsong.getArtist() );
		logger.debug("song artist is " + song.getArtist() );

		song.setUri(uri);
		logger.debug("song uri is " + song.getPlainTextUri() );
		
		song.setGenres(topsong.getGenres().toCSL() );
		logger.debug("song genres is/are " + song.getGenres() );
		 
		song.setWeekending(topsong.getWeeks().getLast() );
		logger.debug("song last week in charts was " + song.getWeekending() );

		song.setTotalweeks(topsong.getWeeks().getWeek().size() );
		logger.debug(" number of weeks at #1 was " + song.getTotalweeks() );
		
		song.setDescription(topsong.stringifyDescr() ); 
		logger.debug(" Song description " + song.getDescription() );

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
