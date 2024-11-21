package com.example.movie.controller;

import com.example.movie.entity.Movie;
import com.example.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @RequestMapping("/")
    public String getHomePage(Model model, @Param("keyword") String keyword) {
        movieService.getHomePage(model, keyword);
        return "index";
    }

    @RequestMapping("/movies")
    public String getMovies(Model model, @Param("keyword") String keyword) {
        movieService.getMovies(model, keyword);
        return "movies";
    }

    @GetMapping("/movies/{id}")
    public String showMovieDetails(@PathVariable("id") Long id, Model model) {
        movieService.showMovieDetails(id, model);
        return "movie_details";
    }

    @RequestMapping("/new")
    public String showNewMovieForm(Model model) {
        movieService.showNewMovieForm(model);
        return "new_movie";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<Object> saveMovie(@RequestBody Movie movie) {
        return movieService.saveMovie(movie);
    }

    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Movie> editMovieById(@PathVariable Long id) {
        return movieService.editMovieById(id);
    }

    @RequestMapping("/delete/{id}")
    public String deleteCourse(@PathVariable(name = "id") Long id) {
        movieService.deleteCourse(id);
        return "redirect:/movies";
    }
}