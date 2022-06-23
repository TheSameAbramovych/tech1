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

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final ArticleRepo articleRepo;

    public List<User> getUserByAge(int age) {
        return userRepo.findAllByAgeGreaterThanEqual(age);
    }

    public List<User> getUserByColor(Color color) {
//        return userRepo.findAllByArticles_Color(color); // It is better?
        return articleRepo.findAllByColor(color).stream()
                .map(Article::getUser)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getUniqueUserName() {
        return userRepo.findAllDistinctNamesWithArticlesMoreThen(3);
    }

    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    public User findByUserId(long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NotFoundUserException(id));
    }

    public User createUser(UserRequest request) {
        User user = User.builder()
                .name(request.getName())
                .age(request.getAge())
                .build();
        return userRepo.save(user);
    }
}
