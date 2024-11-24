package com.example.movie.service.impl;

import com.example.movie.UserContext;
import com.example.movie.entity.Movie;
import com.example.movie.entity.Review;
import com.example.movie.entity.User;
import com.example.movie.repository.MovieRepository;
import com.example.movie.repository.ReviewRepository;
import com.example.movie.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;


    @Override
    public void addReview(Long id, int rating, String text, Model model) {

        User currentUser = UserContext.getInstance().getCurrentUser();
        List<Movie> listMovies = movieRepository.findAll();
        Movie movie = movieRepository.getReferenceById(id);

        if (currentUser != null) {
            Review review = new Review();
            review.setUser(currentUser);
            review.setMovie(movie);
            review.setText(text);
            review.setRating(rating);
            review.setDate(LocalDateTime.now()); // Устанавливаем текущую дату и время
            reviewRepository.save(review);
        }
    }

    @Override
    public void showReviewsPage(Model model) {
        model.addAttribute("reviews", reviewRepository.findAll());
        model.addAttribute("listMovies", movieRepository.findAll());
        User user = UserContext.getInstance().getCurrentUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
    }
}
