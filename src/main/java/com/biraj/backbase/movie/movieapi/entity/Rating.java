package com.biraj.backbase.movie.movieapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Rating implements Serializable {

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @Id
    private Users user;
    @ManyToOne
    @JoinColumn(name="id", nullable=false)
    @Id
    private Movies movie;
    private double rating;
}
