package com.biraj.backbase.movie.movieapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

/**
 * 
 * @author birajmishra
 * User Entity
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Movies {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(nullable = false)
	private int releaseYear;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private boolean isAwarded;
	@Column(nullable = false)
	private String category;
	@OneToMany(mappedBy="movie")
	private Set<MovieRating> movieRatings;
}