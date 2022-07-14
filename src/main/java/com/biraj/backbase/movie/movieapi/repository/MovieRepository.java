package com.biraj.backbase.movie.movieapi.repository;

import com.biraj.backbase.movie.movieapi.entity.Movies;
import com.biraj.backbase.movie.movieapi.entity.Users;
import com.biraj.backbase.movie.movieapi.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author birajmishra
 */
@Repository
public interface MovieRepository extends CrudRepository<Movies, Integer> {
    Optional<Movies> findByName(String name);
    Optional<Movies> findByNameAndReleaseYear(String name, int year);
}