package com.tech1.repository;

import com.tech1.entity.Article;
import com.tech1.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepo extends JpaRepository<Article, Long> {
    List<Article> findAllByColor(Color color);
}

