package com.biraj.backbase.movie.movieapi.repository;

import com.biraj.backbase.movie.movieapi.entity.Movies;
import com.biraj.backbase.movie.movieapi.entity.Rating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author birajmishra
 */
@Repository
public interface RatingRepository extends CrudRepository<Rating, Integer> {



}