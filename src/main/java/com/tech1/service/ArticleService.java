package com.tech1.service;

import com.tech1.controllers.request.ArticleRequest;
import com.tech1.entity.Article;
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
        Article article = new Article();
        article.setUser(userService.findByUserId(request.getUserId()));
        article.setColor(request.getColor());
        article.setText(request.getText());
        return articleRepo.save(article);
    }
}
