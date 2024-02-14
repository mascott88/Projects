package com.openclassrooms.watchlist.repository;

import com.openclassrooms.watchlist.domain.WatchlistItem;

public interface MovieUpdateRepository {

	public void updateMovie(WatchlistItem watchlistItem);

}
