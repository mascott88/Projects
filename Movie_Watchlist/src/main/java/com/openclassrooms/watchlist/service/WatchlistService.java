package com.openclassrooms.watchlist.service;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.watchlist.domain.WatchlistItem;
import com.openclassrooms.watchlist.exception.DuplicateTitleException;
import com.openclassrooms.watchlist.exception.MovieNotFoundException;
import com.openclassrooms.watchlist.exception.PriorityInputException;
import com.openclassrooms.watchlist.exception.RatingInputException;
import com.openclassrooms.watchlist.repository.MovieRepository;

@Service
public class WatchlistService {
	@Autowired
	MovieRepository movieRepository;
	@Autowired
	MovieDirectoryService movieDirectoryService;

	public long getWatchlistItemsSize() {
		return movieRepository.count();
	}

	public Iterable<WatchlistItem> getMovieList() {
		Iterable<WatchlistItem> watchlistItems = movieRepository.findAll();
		return watchlistItems;
	}

	public Optional<WatchlistItem> findWatchlistItemById(Integer id) {
		Optional<WatchlistItem> watchlistItem = movieRepository.findById(id);
		return watchlistItem;
	}

	public WatchlistItem findWatchlistItemByTitle(String title) {
		return movieRepository.getByTitle(title);
	}

	@Transactional
	public void deleteWatchlistItem(@Valid WatchlistItem watchlistItem) {
		movieRepository.deleteMovie(watchlistItem);
	}

	public void addOrUpdateWatchlistItem(WatchlistItem watchlistItem) throws DuplicateTitleException, MovieNotFoundException, RatingInputException, PriorityInputException {
		
		if (!watchlistItem.getRating().isEmpty() && !watchlistItem.getRating().matches("\\d|\\d\\.\\d|10\\.?0?")) {
			throw new RatingInputException();
		}
		if (!watchlistItem.getPriority().matches("[LMHlmh]{1}")) {
			throw new PriorityInputException();
		}
		
		WatchlistItem movie;
		String response = movieDirectoryService.getMovieResponse(watchlistItem.getTitle());

		if (watchlistItem.getId() == null) {
			if (movieRepository.getByTitle(movieDirectoryService.getImdbMovieTitle(watchlistItem.getTitle())) != null) {
				throw new DuplicateTitleException();
			}
			if (!response.equals(null) && response.equals("Movie not found!")) {
				throw new MovieNotFoundException();
			} else {				
				movie = new WatchlistItem();
				movie.setImdbID(movieDirectoryService.getImdbMovieID(watchlistItem.getTitle()));
				movie.setTitle(movieDirectoryService.getImdbMovieTitle(watchlistItem.getTitle()));
				movie.setYear(Integer.valueOf(movieDirectoryService.getMovieYear(watchlistItem.getTitle())));
				movie.setLength(movieDirectoryService.getMovieLength(watchlistItem.getTitle()).substring(0,3));		
				movie.setRating(watchlistItem.getRating());
				movie.setPriority(watchlistItem.getPriority());
				movie.setComment(watchlistItem.getComment());				
				movieRepository.insertMovie(movie);
			}
		} else {			
			if (response.equals("Movie not found!")) {				
				throw new MovieNotFoundException();
			}
			movie = movieRepository.getById(watchlistItem.getId());
			movie.setImdbID(movieDirectoryService.getImdbMovieID(watchlistItem.getTitle()));
			movie.setTitle(movieDirectoryService.getImdbMovieTitle(watchlistItem.getTitle()));
			movie.setYear(Integer.valueOf(movieDirectoryService.getMovieYear(watchlistItem.getTitle())));
			movie.setLength(movieDirectoryService.getMovieLength(watchlistItem.getTitle()).substring(0,3));
			movie.setRating(watchlistItem.getRating());
			movie.setPriority(watchlistItem.getPriority());
			movie.setComment(watchlistItem.getComment());
			movieRepository.save(movie);			
		}
	}
}