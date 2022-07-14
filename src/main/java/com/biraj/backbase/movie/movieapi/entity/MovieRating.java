package com.biraj.backbase.movie.movieapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(indexes = {@Index(name = "rating_index", columnList = "rating DESC")})
public class MovieRating implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private Users user;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name="movie_id", nullable=false)
    private Movies movie;
    private double rating;
}
