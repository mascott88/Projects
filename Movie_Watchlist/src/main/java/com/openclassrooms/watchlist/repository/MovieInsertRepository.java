package com.openclassrooms.watchlist.repository;

import com.openclassrooms.watchlist.domain.WatchlistItem;

public interface MovieInsertRepository {

	public void insertMovie(WatchlistItem watchlistItem);
	
}
