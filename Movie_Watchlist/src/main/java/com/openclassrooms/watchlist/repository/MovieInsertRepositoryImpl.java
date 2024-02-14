package com.openclassrooms.watchlist.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.openclassrooms.watchlist.domain.WatchlistItem;

public class MovieInsertRepositoryImpl implements MovieInsertRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void insertMovie(WatchlistItem watchlistItem) {
		this.entityManager.persist(watchlistItem);
		
	}
}
