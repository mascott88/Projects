package com.openclassrooms.watchlist.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.openclassrooms.watchlist.domain.WatchlistItem;

public class MovieDeleteRepositoryImpl implements MovieDeleteRepository {

	@Autowired
	private EntityManager entityManager;

	@Override
	public void deleteMovie(WatchlistItem watchlistItem) {
		if (entityManager.contains(watchlistItem)) {
			entityManager.remove(watchlistItem);
		} else {			
			entityManager.remove(entityManager.merge(watchlistItem));
		}		
	}
}