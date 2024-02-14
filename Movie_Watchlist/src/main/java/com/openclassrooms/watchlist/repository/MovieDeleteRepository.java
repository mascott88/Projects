package com.openclassrooms.watchlist.repository;

import com.openclassrooms.watchlist.domain.WatchlistItem;

public interface MovieDeleteRepository {

	public void deleteMovie(WatchlistItem watchlistItem);
	
}
