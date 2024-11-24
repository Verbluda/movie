package com.example.movie.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

public interface ReviewService {

    void addReview(Long movieId, int rating, String text, Model model);
    void showReviewsPage(Model model);
}
