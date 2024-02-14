package com.openclassrooms.watchlist.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.openclassrooms.watchlist.domain.WatchlistItem;

public class MovieUpdateRepositoryImpl  implements MovieUpdateRepository {
	@Autowired
	private EntityManager entityManager;
	@Override
	public void updateMovie(WatchlistItem watchlistItem) {
		if (entityManager.contains(watchlistItem)) {
			entityManager.refresh(watchlistItem);
		} else {			
			entityManager.refresh(entityManager.merge(watchlistItem));
		}		
	}
}
