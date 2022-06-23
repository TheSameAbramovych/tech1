package com.tech1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech1.DataProvider;
import com.tech1.controllers.request.ArticleRequest;
import com.tech1.entity.Article;
import com.tech1.entity.Color;
import com.tech1.entity.User;
import com.tech1.repository.ArticleRepo;
import com.tech1.repository.UserRepo;
import com.tech1.service.ArticleService;
import com.tech1.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        ArticleService.class,
        UserService.class,
        ArticleController.class
})
class ArticleControllerTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    private ArticleRepo articleRepo;
    @MockBean
    private UserRepo userRepo;

    @Autowired
    private ArticleController articleController;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(articleController).build();
    }

    @Test
    public void getArticles() throws Exception {
        Article articleFirst = DataProvider.getArticle(null);
        Article articleSecond = DataProvider.getArticle(null);

        List<Article> articles = new ArrayList<>();
        articles.add(articleFirst);
        articles.add(articleSecond);

        when(articleRepo.findAll())
                .thenReturn(articles);

        mvc.perform(MockMvcRequestBuilders
                        .get("/articles")
//                        .param("age", "3")
//                        .param("color", Color.RED.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.[*]").exists(),
                        MockMvcResultMatchers.jsonPath("$.[0].id").value(articleFirst.getId()),
                        MockMvcResultMatchers.jsonPath("$.[0].text").value(articleFirst.getText()),
                        MockMvcResultMatchers.jsonPath("$.[0].color").value(articleFirst.getColor().toString()),
                        MockMvcResultMatchers.jsonPath("$.[1].id").value(articleSecond.getId()),
                        MockMvcResultMatchers.jsonPath("$.[1].text").value(articleSecond.getText()),
                        MockMvcResultMatchers.jsonPath("$.[1].color").value(articleSecond.getColor().toString())
                );
    }

    @Test
    public void createArticles() throws Exception {
        User user = DataProvider.getUser(null);
        Article article = DataProvider.getArticle(null);
        ArticleRequest articleRequest =
                new ArticleRequest(UUID.randomUUID().toString(), Color.RED, user.getId());

        ArgumentCaptor<Article> articleArgumentCaptor = ArgumentCaptor.forClass(Article.class);

        when(userRepo.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(articleRepo.save(articleArgumentCaptor.capture()))
                .thenReturn(article);

        mvc.perform(MockMvcRequestBuilders
                        .post("/articles/create")
                        .content(toJson(articleRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.id").value(article.getId()),
                        MockMvcResultMatchers.jsonPath("$.text").value(article.getText()),
                        MockMvcResultMatchers.jsonPath("$.color").value(article.getColor().toString())
                );

        assertThat(articleArgumentCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(Article.builder()
                        .text(articleRequest.getText())
                        .color(articleRequest.getColor())
                        .user(user)
                        .build());
    }

    @SneakyThrows
    private String toJson(ArticleRequest articleRequest) {
        return OBJECT_MAPPER.writeValueAsString(articleRequest);
    }
}