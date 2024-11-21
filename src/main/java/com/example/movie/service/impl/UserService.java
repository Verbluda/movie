package com.example.movie.service.impl;


import com.example.movie.entity.User;
import com.example.movie.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    // Метод для получения пользователя по ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
    // Метод для получения всех пользователей с ролью "teacher"
    public List<User> getAllTeachers() {
        return userRepository.findByRole("teacher");
    }

    public void registerTeacher(User user) {
        user.setRole("teacher"); // Установка роли teacher
        userRepository.save(user);
    }

}
