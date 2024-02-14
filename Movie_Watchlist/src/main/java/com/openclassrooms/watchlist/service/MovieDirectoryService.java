package com.openclassrooms.watchlist.service;

public interface MovieDirectoryService {

	String getMovieYear(String title);
	
	String getMovieLength(String title);

	String getMovieResponse(String title);	

	String getImdbMovieTitle(String title);

	String getImdbMovieID(String title);
	
}