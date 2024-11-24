package com.example.movie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "favorite")
public class FavoriteMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long movieId;

    private Long userId;

    public FavoriteMovie(Long movieId, Long userId) {
        this.movieId = movieId;
        this.userId = userId;
    }

    public FavoriteMovie() {
    }
}
