package com.example.movie.service;

import com.example.movie.entity.Movie;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface MovieService {

    void getHomePage(Model model, String keyword);
    void getMovies(Model model, String keyword);
    void showMovieDetails(Long id, Model model);
    void showNewMovieForm(Model model);
    ResponseEntity<Object> saveMovie(Movie movie);
    ResponseEntity<Movie> editMovieById(Long id);
    void deleteCourse(Long id);
}
