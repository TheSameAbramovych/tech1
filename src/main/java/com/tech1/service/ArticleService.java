package com.tech1.service;

import com.tech1.entity.Article;
import com.tech1.repository.ArticleRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ArticleService {
   private final ArticleRepo articleRepo;

   public List<Article> getAll(){
      return articleRepo.findAll();
   }

   public void createArticle(Article article){
      articleRepo.save(article);
   }
}
