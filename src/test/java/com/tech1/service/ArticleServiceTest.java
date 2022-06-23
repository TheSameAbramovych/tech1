package com.tech1.service;

import com.tech1.DataProvider;
import com.tech1.controllers.request.ArticleRequest;
import com.tech1.entity.Article;
import com.tech1.entity.Color;
import com.tech1.entity.User;
import com.tech1.exception.NotFoundUserException;
import com.tech1.repository.ArticleRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepo articleRepo;
    @Mock
    private UserService userService;

    @InjectMocks
    private ArticleService articleService;

    @Test
    public void getAll() {
        List<Article> articles = Collections.singletonList(DataProvider.getArticle(null));
        when(articleRepo.findAll())
                .thenReturn(articles);

        List<Article> result = articleService.getAll();

        assertThat(articles)
                .isEqualTo(result);
    }

    @Test
    public void createArticle() {
        User user = DataProvider.getUser(null);
        Article article = DataProvider.getArticle(null);
        ArticleRequest articleRequest =
                new ArticleRequest(UUID.randomUUID().toString(), Color.RED, user.getId());

        ArgumentCaptor<Article> articleArgumentCaptor = ArgumentCaptor.forClass(Article.class);
        when(userService.findByUserId(user.getId()))
                .thenReturn(user);
        when(articleRepo.save(articleArgumentCaptor.capture()))
                .thenReturn(article);

        Article result = articleService.createArticle(articleRequest);

        assertAll(
                () -> assertThat(articleArgumentCaptor.getValue())
                        .usingRecursiveComparison()
                        .isEqualTo(Article.builder()
                                .text(articleRequest.getText())
                                .color(articleRequest.getColor())
                                .user(user)
                                .build()),
                () -> assertThat(article)
                        .usingRecursiveComparison()
                        .isEqualTo(result)
        );
    }

    @Test
    public void createArticle_userNotFound() {
        ArticleRequest articleRequest =
                new ArticleRequest(UUID.randomUUID().toString(), Color.RED, -1L);

        when(userService.findByUserId(anyLong()))
                .thenThrow(new NotFoundUserException(articleRequest.getUserId()));

        assertThrows(NotFoundUserException.class, () -> articleService.createArticle(articleRequest));

        verify(articleRepo, never()).save(any());
    }
}
