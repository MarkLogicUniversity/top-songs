package com.marklogic.training;

import com.marklogic.training.model.Song;

public interface SongBuilder {
		
	public Song getSongDetails(String uri);
	
	public Song getSong(String uri);
	
	public void setConnection(MarkLogicConnection conn);
	
	public void insertSong(String title, String artist, String album, String genres, String writers, 
			String producers, String label, String description,String weeks);

}
