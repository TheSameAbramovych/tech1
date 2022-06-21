package com.tech1.controllers;

import com.tech1.controllers.request.ArticleRequest;
import com.tech1.entity.Article;
import com.tech1.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/create")
    public Article createArticle(@RequestBody ArticleRequest request) {
        return articleService.createArticle(request);
    }

    @GetMapping
    public List<Article> getAll() {
        return articleService.getAll();
    }
}