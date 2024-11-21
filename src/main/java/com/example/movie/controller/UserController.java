package com.example.movie.controller;

import com.example.movie.entity.Review;
import com.example.movie.entity.User;
import com.example.movie.UserContext;
import com.example.movie.service.impl.ReviewService;
import com.example.movie.service.impl.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;

    public UserController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/login")
    public String index(HttpServletRequest request) {
        String redirectUrl = request.getHeader("Referer");
        request.getSession().setAttribute("redirectUrl", redirectUrl);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request) {

        Optional<User> user = userService.findUserByUsername(username);

        if (user.isPresent()) {
            if (BCrypt.checkpw(password, user.get().getPassword())) {
                UserContext.getInstance().setCurrentUser(user.get());

                String redirectUrl = (String) request.getSession().getAttribute("redirectUrl");
                if (redirectUrl != null) {
                    return "redirect:" + redirectUrl;
                }
            }
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegistrationForm(HttpServletRequest request, Model model) {
        String redirectUrl = request.getHeader("Referer");
        request.getSession().setAttribute("redirectUrl", redirectUrl);

        if (!model.containsAttribute("username")) {
            model.addAttribute("username", "");
        }
        if (!model.containsAttribute("email")) {
            model.addAttribute("email", "");
        }
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

        String redirectUrl = request.getHeader("Referer");
        request.getSession().setAttribute("redirectUrl", redirectUrl);

        Optional<User> userByEmail = userService.findByEmail(email);
        Optional<User> userByUsername = userService.findByUsername(username);

        if (userByUsername.isPresent()) {
            model.addAttribute("username", "Имя уже занято");
            return "redirect:" + redirectUrl;
        }

        if (userByEmail.isPresent()) {
            model.addAttribute("email", "Email уже используется");
            return "redirect:" + redirectUrl;
        }

        userService.saveUser(new User(username, password, email, firstName, lastName));

        Optional<User> newUser = userService.findUserByUsername(username);

        if (newUser.isPresent()) {
            UserContext.getInstance().setCurrentUser(newUser.get());
        }

        if (redirectUrl != null) {
            return "redirect:" + redirectUrl;
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        String redirectUrl = request.getHeader("Referer");
        UserContext.getInstance().clearCurrentUser();

        if (redirectUrl != null && redirectUrl.contains("/profile")) {
            return "redirect:/";
        }

        if (redirectUrl != null) {
            return "redirect:" + redirectUrl;
        }

        return "redirect:/";
    }

    @GetMapping("/profile/{id}")
    public String getUserProfile(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/edit_profile")
    public String editProfile(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
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

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            model.addAttribute("user", userService.getUserById(id)); // добавляем пользователя в модель
            return "redirect:/profile/" + id;
        }

        User user = userService.getUserById(id);
        if (user != null) {
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);

            if (!password.isEmpty()) {
                user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            }

            userService.saveUser(user);
        }

        return "redirect:/profile/" + id;
    }

    @GetMapping("/reviews")
    public String showReviewsPage(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        User user = UserContext.getInstance().getCurrentUser();
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "reviews"; // reviews.html
    }

    @PostMapping("/addReview")
    public String addReview(@RequestParam String text) {
        User currentUser = UserContext.getInstance().getCurrentUser();

        if (currentUser != null) {
            Review review = new Review();
            review.setUser(currentUser);
            review.setText(text);
            review.setDate(LocalDateTime.now()); // Устанавливаем текущую дату и время
            reviewService.saveReview(review);
        }
        return "redirect:/reviews";
    }

}
