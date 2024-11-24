package com.example.movie.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

public interface UserService {

    void index(HttpServletRequest request);
    String login(String username, String password, HttpServletRequest request);
    void showRegistrationForm(HttpServletRequest request, Model model);
    String registerUser(String username, String password, String email, String firstName, String lastName, HttpServletRequest request, Model model);
    void getUserProfile(Long id, Model model);
    void editProfile(Long id, Model model);
    String logout(HttpServletRequest request);
    String updateProfile(Long id, String username, String firstName, String lastName, String email, String password, String confirmPassword, Model model);
}
