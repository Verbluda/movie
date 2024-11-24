package com.example.movie.service.impl;

import com.example.movie.UserContext;
import com.example.movie.entity.Movie;
import com.example.movie.entity.User;
import com.example.movie.repository.MovieRepository;
import com.example.movie.repository.UserRepository;
import com.example.movie.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Override
    public void index(HttpServletRequest request) {
        String redirectUrl = request.getHeader("Referer");
        request.getSession().setAttribute("redirectUrl", redirectUrl);
    }

    @Override
    public String login(String username, String password, HttpServletRequest request) {
        Optional<User> user = userRepository.findByUsername(username);

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

    @Override
    public void showRegistrationForm(HttpServletRequest request, Model model) {
        String redirectUrl = request.getHeader("Referer");
        request.getSession().setAttribute("redirectUrl", redirectUrl);

        if (!model.containsAttribute("username")) {
            model.addAttribute("username", "");
        }
        if (!model.containsAttribute("email")) {
            model.addAttribute("email", "");
        }
    }

    @Override
    public String registerUser(String username, String password, String email, String firstName, String lastName, HttpServletRequest request, Model model) {
        String redirectUrl = request.getHeader("Referer");
        request.getSession().setAttribute("redirectUrl", redirectUrl);

        Optional<User> userByEmail = userRepository.findByEmail(email);
        Optional<User> userByUsername = userRepository.findByUsername(username);

        if (userByUsername.isPresent()) {
            model.addAttribute("username", "Имя уже занято");
            return "redirect:" + redirectUrl;
        }

        if (userByEmail.isPresent()) {
            model.addAttribute("email", "Email уже используется");
            return "redirect:" + redirectUrl;
        }

        userRepository.save(new User(username, password, email, firstName, lastName));

        Optional<User> newUser = userRepository.findByUsername(username);

        if (newUser.isPresent()) {
            UserContext.getInstance().setCurrentUser(newUser.get());
        }

        if (redirectUrl != null) {
            return "redirect:" + redirectUrl;
        }

        return "redirect:/";
    }

    @Override
    public void getUserProfile(Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);

        List<Movie> listFavoriteMovies = movieRepository.findFavoriteMovieByUser(user.getId());
        model.addAttribute("listFavoriteMovies", listFavoriteMovies);
    }

    @Override
    public void editProfile(Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);
    }

    @Override
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

    @Override
    public String updateProfile(Long id, String username, String firstName, String lastName, String email, String password, String confirmPassword, Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            model.addAttribute("user", userRepository.findById(id).orElse(null)); // добавляем пользователя в модель
            return "redirect:/profile/" + id;
        }

        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);

            if (!password.isEmpty()) {
                user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            }

            userRepository.save(user);
        }

        return "redirect:/profile/" + id;
    }
}
