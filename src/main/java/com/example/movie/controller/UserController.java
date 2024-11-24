package com.example.movie.controller;

import com.example.movie.service.ReviewService;
import com.example.movie.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;

    @GetMapping("/login")
    public String index(HttpServletRequest request) {
        userService.index(request);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request) {
        return userService.login(username, password, request);
    }

    @GetMapping("/register")
    public String showRegistrationForm(HttpServletRequest request, Model model) {
        userService.showRegistrationForm(request, model);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               HttpServletRequest request,
                               Model model) {
        return userService.registerUser(username, password, email, firstName, lastName, request, model);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        return userService.logout(request);
    }

    @GetMapping("/profile/{id}")
    public String getUserProfile(@PathVariable("id") Long id, Model model) {
        userService.getUserProfile(id, model);
        return "profile";
    }

    @GetMapping("/author")
    public String getAuthorProfile() {
        return "author";
    }

    @GetMapping("/edit_profile")
    public String editProfile(@PathVariable("id") Long id, Model model) {
        userService.editProfile(id, model);
        return "redirect:/profile/" + id;
    }

    @PostMapping("/edit_profile/{id}")
    public String updateProfile(@PathVariable("id") Long id,
                                @RequestParam("username") String username,
                                @RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("email") String email,
                                @RequestParam("password") String password,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {
        return userService.updateProfile(id, username, firstName, lastName, email, password, confirmPassword, model);
    }

    @GetMapping("/reviews")
    public String showReviewsPage(Model model) {
        reviewService.showReviewsPage(model);
        return "reviews"; // reviews.html
    }

    @PostMapping("/addReview")
    public String addReview(@RequestParam Long movieId, @RequestParam int rating, @RequestParam String text, Model model) {
        reviewService.addReview(movieId, rating, text, model);
        return "redirect:/reviews";
    }
}
