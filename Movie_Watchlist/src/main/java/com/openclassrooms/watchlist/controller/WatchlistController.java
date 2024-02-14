package com.openclassrooms.watchlist.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.openclassrooms.watchlist.domain.WatchlistItem;
import com.openclassrooms.watchlist.exception.DuplicateTitleException;
import com.openclassrooms.watchlist.exception.MovieNotFoundException;
import com.openclassrooms.watchlist.exception.PriorityInputException;
import com.openclassrooms.watchlist.exception.RatingInputException;
import com.openclassrooms.watchlist.service.WatchlistService;

@Controller
public class WatchlistController {

	Logger logger = LoggerFactory.getLogger(WatchlistController.class);

	private WatchlistService watchlistService;

	@Autowired
	public WatchlistController(WatchlistService watchlistService) {
		super();
		this.watchlistService = watchlistService;
	}

	@GetMapping("/watchlistItemForm")
	public ModelAndView showWatchlistItemForm(@RequestParam(required = false) Integer id) {

		logger.info("GET /watchlistItemForm called");

		String viewName = "watchlistItemForm";

		Map<String, Object> model = new HashMap<String, Object>();

		if (id == null) {
			model.put("watchlistItem", new WatchlistItem());
		} else {
			Optional<WatchlistItem> watchlistItem = watchlistService.findWatchlistItemById(id);
			model.put("watchlistItem", watchlistItem);
		}

		return new ModelAndView(viewName, model);
	}

	@GetMapping("/watchlist")
	public ModelAndView getWatchlist(@RequestParam(required = false) Integer id) {

		logger.info("GET /watchlist called");
		String viewName = "watchlist";
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("watchlistItems", watchlistService.getMovieList());
		model.put("numberOfMovies", watchlistService.getWatchlistItemsSize());

		return new ModelAndView(viewName, model);
	}

	@PostMapping("/watchlistItemForm")
	public ModelAndView submitWatchlistItemForm(@Valid WatchlistItem watchlistItem, BindingResult bindingResult, @RequestParam(name = "remove", required = false) String remove) throws MovieNotFoundException, DuplicateTitleException {
		
		logger.info("POST /watchlistItemForm called");
		
		if (bindingResult.hasErrors()) {
			return new ModelAndView("watchlistItemForm");
		}
		if ((watchlistService.getWatchlistItemsSize()) + 1 > 100) {
			bindingResult.reject(null, "Your list is full, no more movies can be added!");
			return new ModelAndView("watchlistItemForm");
		}
		if(remove == null) {
			try {
				watchlistService.addOrUpdateWatchlistItem(watchlistItem);
			} catch (DuplicateTitleException e) {
				bindingResult.rejectValue("title", "", "This movie is already on your list!");
				return new ModelAndView("watchlistItemForm");
			} catch (MovieNotFoundException e) {
				bindingResult.reject(null, "This title wasn't found in the IMDb!");
				return new ModelAndView("watchlistItemForm");
			} catch (RatingInputException e) {
				bindingResult.reject(null, "Rating should be 1.0-10.0 only");
				return new ModelAndView("watchlistItemForm");
			} catch (PriorityInputException e) {
				bindingResult.reject(null, "Priority should be L, M, or H only");
				return new ModelAndView("watchlistItemForm");
			}
			
			RedirectView redirect = new RedirectView();
			redirect.setUrl("/watchlist");
			return new ModelAndView(redirect);
		}		
		List<WatchlistItem> movies = (List<WatchlistItem>) watchlistService.getMovieList();
		if (!(remove == null) && remove.equals("remove")) {
			for (WatchlistItem x : movies) {
				if (x.getTitle().equals(watchlistItem.getTitle())) {					
					watchlistService.deleteWatchlistItem(x);
					RedirectView redirect = new RedirectView();
					redirect.setUrl("/watchlist");
					return new ModelAndView(redirect);					
				}
			}
		}
		return new ModelAndView("watchlistItemForm");						
	}
}