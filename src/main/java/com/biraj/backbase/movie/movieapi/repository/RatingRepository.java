package com.biraj.backbase.movie.movieapi.repository;

import com.biraj.backbase.movie.movieapi.bean.TopMovies;
import com.biraj.backbase.movie.movieapi.entity.MovieRating;
import com.biraj.backbase.movie.movieapi.entity.Movies;
import com.biraj.backbase.movie.movieapi.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author birajmishra
 */
@Repository
public interface RatingRepository extends PagingAndSortingRepository<MovieRating, Integer> {
    Optional<MovieRating> findByMovieAndUser(Movies movie, Users user);
   @Query("SELECT new com.biraj.backbase.movie.movieapi.bean.TopMovies(m.name,AVG(mr.rating),m.releaseYear) FROM MovieRating AS mr INNER JOIN Movies AS m on m.id=mr.movie GROUP BY m.name ORDER BY AVG(mr.rating) DESC")
    Optional<List<TopMovies>> findTopNByRating(Pageable pageable);
}