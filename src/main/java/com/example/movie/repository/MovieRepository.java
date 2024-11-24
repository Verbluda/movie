package com.example.movie.repository;

import com.example.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m WHERE CONCAT(m.movieName, ' ', m.movieDescription, ' ', m.movieDirector, ' ', m.movieGenre, ' ') LIKE CONCAT('%', :searchTerm, '%')")
    List<Movie> search(String searchTerm);

    @Query("SELECT m FROM Movie m WHERE (SELECT COUNT(r.movie) FROM Review r WHERE r.movie.id = m.id) > 2")
    List<Movie> findPopularMovie();

    @Query("SELECT m FROM Movie m WHERE m.id IN (SELECT f.movieId FROM FavoriteMovie f WHERE f.userId = :id)")
    List<Movie> findFavoriteMovieByUser(Long id);
}
