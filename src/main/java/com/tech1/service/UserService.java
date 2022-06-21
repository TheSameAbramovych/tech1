package com.tech1.service;

import com.tech1.controllers.request.UserRequest;
import com.tech1.entity.Article;
import com.tech1.entity.Color;
import com.tech1.entity.User;
import com.tech1.exception.NotFoundUserException;
import com.tech1.repository.ArticleRepo;
import com.tech1.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final ArticleRepo articleRepo;

    public List<User> getUserByAge(int age) {
        List<User> users = userRepo.findAll();
        return users.stream().filter(user -> user.getAge() >= age).collect(Collectors.toList());
    }

    public List<User> getUserByColor(Color color) {
        List<Article> articles = articleRepo.findAllByColor(color);
        List<User> users = new ArrayList<>();
        articles.forEach(article -> users.add(article.getUser()));
        return users;
    }

    public List<String> getUniqueUserName() {
        return userRepo.findAll().stream()
                .filter(user -> user.getArticles().size() >= 3).map(User::getName).collect(Collectors.toList());
    }

    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    public User findByUserId(long id) {
        User user = userRepo.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundUserException(id);
        }
        return user;
    }

    public User createUser(UserRequest request) {
        User user = new User();
        user.setAge(request.getAge());
        user.setName(request.getName());
        return userRepo.save(user);
    }

}
