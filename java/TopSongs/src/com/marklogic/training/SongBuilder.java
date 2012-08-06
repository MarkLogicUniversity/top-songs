package com.marklogic.training;

import com.marklogic.training.model.Song;

public interface SongBuilder {
	
	public Song getSongDetails(String uri);
	
	public Song getSong(String uri);


}
