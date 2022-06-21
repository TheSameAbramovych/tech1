package com.tech1.controllers;

import com.tech1.controllers.request.ArticleRequest;
import com.tech1.entity.Article;
import com.tech1.entity.Color;
import com.tech1.entity.User;
import com.tech1.service.ArticleService;
import com.tech1.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;

    @PostMapping("/create")
    public void createArticle(@RequestBody ArticleRequest request) {
        Article article = new Article();
        article.setUser(userService.findByUserId(request.getUserId()));
        article.setColor(request.getColor());
        article.setText(request.getText());
        articleService.createArticle(article);
    }

    @GetMapping
    public List<Article> getAll(){
        return articleService.getAll();
    }
}