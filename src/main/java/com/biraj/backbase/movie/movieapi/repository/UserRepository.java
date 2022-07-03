package com.biraj.backbase.movie.movieapi.repository;

import com.biraj.backbase.movie.movieapi.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * @author birajmishra
 */
@Repository
public interface UserRepository extends CrudRepository<Users, Integer> {

    Optional<Users> findByUserId(String userid);
}