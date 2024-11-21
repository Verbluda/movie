package com.example.movie.service.impl;

import com.example.movie.entity.Review;
import com.example.movie.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

}

