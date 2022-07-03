package com.biraj.backbase.movie.movieapi.repository;

import com.biraj.backbase.movie.movieapi.entity.MovieRating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author birajmishra
 */
@Repository
public interface RatingRepository extends CrudRepository<MovieRating, Integer> {
}