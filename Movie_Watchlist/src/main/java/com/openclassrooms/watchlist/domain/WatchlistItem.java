package com.openclassrooms.watchlist.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="watchlist")
public class WatchlistItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer year;

	private String length;

	@NotBlank(message = "Please enter the title")
	@Size(max = 50, message = "Title should be maximum 50 characters")
	private String title;

	private String rating;

	private String priority;
	
	private String imdbID;

	@Size(max = 50, message = "Comment should be maximum 50 characters")
	private String comment;
	
	public WatchlistItem() {
		super();
	}
	
	public WatchlistItem(String title, String rating, String priority, String comment, Integer year, String length) {
		super();
		this.title = title;
		this.length = length;
		this.rating = rating;
		this.priority = priority;
		this.year = year;
		this.comment = comment;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getImdbID() {
		return imdbID;
	}

	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer integer) {
		this.year = integer;
	}

	public Integer getId() {
		return id;
	}

	//public void setId(Integer id) {
	//	this.id = id;
	//}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority.toUpperCase();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return "WatchlistItem [id=" + id + ", year=" + year + ", length=" + length
				+ ", rating=" + rating + ", priority=" + priority + ", comment=" + comment + "]";
	}
}