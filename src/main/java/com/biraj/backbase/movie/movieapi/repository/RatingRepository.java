package com.biraj.backbase.movie.movieapi.repository;

import com.biraj.backbase.movie.movieapi.bean.TopMovies;
import com.biraj.backbase.movie.movieapi.entity.MovieRating;
import com.biraj.backbase.movie.movieapi.entity.Movies;
import com.biraj.backbase.movie.movieapi.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author birajmishra
 */
@Repository
public interface RatingRepository extends CrudRepository<MovieRating, Integer> {
    Optional<MovieRating> findByMovieAndUser(Movies movie, Users user);

    //@Query(value = "SELECT M.NAME, AVG(RATING) AS RATING FROM MOVIE_RATING MR INNER JOIN MOVIES M ON M.ID=MR.MOVIE_ID GROUP BY M.NAME", nativeQuery = true)
    @Query("SELECT new com.biraj.backbase.movie.movieapi.bean.TopMovies(m.name,AVG(mr.rating)) FROM MovieRating AS mr INNER JOIN Movies AS m on m.id=mr.movie GROUP BY m.name")
    Optional<List<TopMovies>> findTop10ByRating();
}

//SELECT M.NAME , AVG(RATING)  AS RATING FROM MOVIE_RATING MR  INNER JOIN MOVIES M ON M.ID=MR.MOVIE_ID GROUP BY M.NAME