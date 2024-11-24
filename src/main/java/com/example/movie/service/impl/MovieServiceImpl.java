package com.example.movie.service.impl;

import com.example.movie.UserContext;
import com.example.movie.entity.FavoriteMovie;
import com.example.movie.entity.Movie;
import com.example.movie.entity.Review;
import com.example.movie.entity.User;
import com.example.movie.repository.FavoriteMovieRepository;
import com.example.movie.repository.MovieRepository;
import com.example.movie.repository.ReviewRepository;
import com.example.movie.repository.UserRepository;
import com.example.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteMovieRepository favoriteMovieRepository;

    @Override
    public void getHomePage(Model model, String keyword) {
        List<Movie> listMovies = listAll(keyword);
        model.addAttribute("listMovies", listMovies);
        model.addAttribute("keyword", keyword);

        List<Review> reviews = reviewRepository.findTopNByOrderByDateDesc(3);  // Получаем последние 3 отзыва
        model.addAttribute("reviews", reviews);

        List<Movie> movies = movieRepository.findPopularMovie();
        model.addAttribute("movies", movies);

        // Получаем текущего пользователя из UserContext и добавляем его в модель
        User user = UserContext.getInstance().getCurrentUser();
        if (user != null) { // Проверяем, что пользователь существует
            model.addAttribute("user", user);
        }
    }

    @Override
    public void getMovies(Model model, String keyword) {
        List<Movie> listMovies = listAll(keyword);
        model.addAttribute("listMovies", listMovies);
        model.addAttribute("keyword", keyword);

        // Проверка на наличие фильмов
        boolean noMoviesFound = listMovies.isEmpty();
        model.addAttribute("noMoviesFound", noMoviesFound);

        // Получаем текущего пользователя из UserContext и добавляем его в модель
        User user = UserContext.getInstance().getCurrentUser();
        if (user != null) {
            model.addAttribute("user", user);
            List<Movie> listFavoriteMovie = movieRepository.findFavoriteMovieByUser(user.getId());
            model.addAttribute("listFavoriteMovie", listFavoriteMovie);
        }
    }

    @Override
    public void getAuthorInfo(Model model) {
        // Получаем текущего пользователя из UserContext и добавляем его в модель
        User user = UserContext.getInstance().getCurrentUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

    private List<Movie> listAll(String keyword) {
        if (keyword != null) {
            return movieRepository.search(keyword);
        }
        return movieRepository.findAll();
    }

    @Override
    public void showMovieDetails(Long id, Model model) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie Id:" + id));
        model.addAttribute("movie", movie);

        User user = UserContext.getInstance().getCurrentUser();
        if (user != null) { // Проверяем, что пользователь существует
            model.addAttribute("user", user);
        }
    }

    @Override
    public void showNewMovieForm(Model model) {
        Movie movie = new Movie();
        model.addAttribute("movies", movie);
    }

    @Override
    public ResponseEntity<Object> saveMovie(Movie movie) {
        movieRepository.save(movie);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Movie> editMovieById(Long id) {
        Movie movie = movieRepository.findById(id).get();
        if (movie == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        return ResponseEntity.ok(movie);
    }

    @Override
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Override
    public void makeMovieFavorite(Long id) {
        User user = UserContext.getInstance().getCurrentUser();
        FavoriteMovie favoriteMovie = new FavoriteMovie(id, user.getId());
        favoriteMovieRepository.save(favoriteMovie);
    }
}
