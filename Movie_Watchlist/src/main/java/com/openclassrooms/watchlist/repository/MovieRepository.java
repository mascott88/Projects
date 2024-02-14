package com.openclassrooms.watchlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.watchlist.domain.WatchlistItem;

@Repository
public interface MovieRepository extends JpaRepository<WatchlistItem, Integer>, MovieDeleteRepository, MovieInsertRepository {
	
	WatchlistItem getByTitle(String title);
	
	WatchlistItem getById(Integer id);

	public void insertMovie(WatchlistItem movie);

	public void deleteMovie(WatchlistItem movie);

}