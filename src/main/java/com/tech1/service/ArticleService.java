package com.tech1.service;

import com.tech1.controllers.request.ArticleRequest;
import com.tech1.entity.Article;
import com.tech1.entity.User;
import com.tech1.repository.ArticleRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArticleService {
    private final ArticleRepo articleRepo;
    private final UserService userService;

    public List<Article> getAll() {
        return articleRepo.findAll();
    }

    public Article createArticle(ArticleRequest request) {
        User user = userService.findByUserId(request.getUserId());
        Article article = Article.builder()
                .user(user)
                .color(request.getColor())
                .text(request.getText())
                .build();
        return articleRepo.save(article);
    }
}
