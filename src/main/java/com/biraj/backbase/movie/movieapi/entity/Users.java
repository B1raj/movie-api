package com.biraj.backbase.movie.movieapi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class Users {
	@Id
	private String userId;
	private String password;

	@OneToMany(mappedBy="user")
	private Set<MovieRating> movieRatings;
}