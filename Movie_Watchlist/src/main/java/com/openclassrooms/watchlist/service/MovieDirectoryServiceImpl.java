package com.openclassrooms.watchlist.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MovieDirectoryServiceImpl implements MovieDirectoryService {

	String apiUrl = "http://www.omdbapi.com/?apikey=3606d870&t=";
	Logger logger = LoggerFactory.getLogger(MovieDirectoryServiceImpl.class);

	@Override
	public String getMovieYear(String title) {

		try {
			RestTemplate template = new RestTemplate();

			ResponseEntity<ObjectNode> response = template.getForEntity(apiUrl + title, ObjectNode.class);

			ObjectNode jsonObject = response.getBody();

			return jsonObject.path("Year").asText();
		} catch (Exception e) {
			logger.warn("Something went wront while calling OMDb API" + e.getMessage());
			return null;
		}
	}

	@Override
	public String getMovieLength(String title) {

		try {
			RestTemplate template = new RestTemplate();

			ResponseEntity<ObjectNode> response = template.getForEntity(apiUrl + title, ObjectNode.class);

			ObjectNode jsonObject = response.getBody();

			return jsonObject.path("Runtime").asText();
		} catch (Exception e) {
			logger.warn("Something went wront while calling OMDb API" + e.getMessage());
			return null;
		}
	}

	@Override
	public String getMovieResponse(String title) {

		try {
			RestTemplate template = new RestTemplate();

			ResponseEntity<ObjectNode> response = template.getForEntity(apiUrl + title, ObjectNode.class);

			ObjectNode jsonObject = response.getBody();

			return jsonObject.path("Error").asText();
		} catch (Exception e) {
			logger.warn("Something went wront while calling OMDb API" + e.getMessage());
			return null;
		}
	}

	@Override
	public String getImdbMovieTitle(String title) {

		try {
			RestTemplate template = new RestTemplate();

			ResponseEntity<ObjectNode> response = template.getForEntity(apiUrl + title, ObjectNode.class);

			ObjectNode jsonObject = response.getBody();

			return jsonObject.path("Title").asText();
		} catch (Exception e) {
			logger.warn("Something went wront while calling OMDb API" + e.getMessage());
			return null;
		}
	}
	
	@Override
	public String getImdbMovieID(String title) {

		try {
			RestTemplate template = new RestTemplate();

			ResponseEntity<ObjectNode> response = template.getForEntity(apiUrl + title, ObjectNode.class);

			ObjectNode jsonObject = response.getBody();

			return jsonObject.path("imdbID").asText();
		} catch (Exception e) {
			logger.warn("Something went wront while calling OMDb API" + e.getMessage());
			return null;
		}
	}
}