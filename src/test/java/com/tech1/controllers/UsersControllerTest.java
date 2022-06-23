package com.tech1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech1.DataProvider;
import com.tech1.controllers.request.UserRequest;
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
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        ArticleService.class,
        UserService.class,
        UsersController.class
})
class UsersControllerTest {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    private ArticleRepo articleRepo;
    @MockBean
    private UserRepo userRepo;

    @Autowired
    private UsersController UsersController;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(UsersController).build();
    }

    @Test
    public void getUsers() throws Exception {
        User userFirst = DataProvider.getUser(null);
        User userSecond = DataProvider.getUser(null);

        List<User> users = new ArrayList<>();
        users.add(userFirst);
        users.add(userSecond);

        when(userRepo.findAll())
                .thenReturn(users);

        mvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.[*]").exists(),
                        MockMvcResultMatchers.jsonPath("$.[0].id").value(userFirst.getId()),
                        MockMvcResultMatchers.jsonPath("$.[0].age").value(userFirst.getAge()),
                        MockMvcResultMatchers.jsonPath("$.[0].name").value(userFirst.getName()),
                        MockMvcResultMatchers.jsonPath("$.[1].id").value(userSecond.getId()),
                        MockMvcResultMatchers.jsonPath("$.[1].age").value(userSecond.getAge()),
                        MockMvcResultMatchers.jsonPath("$.[1].name").value(userSecond.getName())
                );
    }

    @Test
    public void getUsersByAge() throws Exception {
        User userFirst = DataProvider.getUser(null);
        User userSecond = DataProvider.getUser(null);

        List<User> users = new ArrayList<>();
        users.add(userFirst);
        users.add(userSecond);

        when(userRepo.findAllByAgeGreaterThanEqual(3))
                .thenReturn(users);

        mvc.perform(MockMvcRequestBuilders
                        .get("/users/")
                        .param("age", "3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.[*]").exists(),
                        MockMvcResultMatchers.jsonPath("$.[0].id").value(userFirst.getId()),
                        MockMvcResultMatchers.jsonPath("$.[0].age").value(userFirst.getAge()),
                        MockMvcResultMatchers.jsonPath("$.[0].name").value(userFirst.getName()),
                        MockMvcResultMatchers.jsonPath("$.[1].id").value(userSecond.getId()),
                        MockMvcResultMatchers.jsonPath("$.[1].age").value(userSecond.getAge()),
                        MockMvcResultMatchers.jsonPath("$.[1].name").value(userSecond.getName())
                );
    }

    @Test
    public void getUsersByColor() throws Exception {
        User userFirst = DataProvider.getUser(null);
        User userSecond = DataProvider.getUser(null);

        List<Article> articles = new ArrayList<>();
        articles.add(DataProvider.getArticle(userFirst));
        articles.add(DataProvider.getArticle(userSecond));
        articles.add(DataProvider.getArticle(userSecond));

        when(articleRepo.findAllByColor(Color.RED))
                .thenReturn(articles);

        mvc.perform(MockMvcRequestBuilders
                        .get("/users/")
                        .param("color", Color.RED.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.[*]").exists(),
                        MockMvcResultMatchers.jsonPath("$.[0].id").value(userFirst.getId()),
                        MockMvcResultMatchers.jsonPath("$.[0].age").value(userFirst.getAge()),
                        MockMvcResultMatchers.jsonPath("$.[0].name").value(userFirst.getName()),
                        MockMvcResultMatchers.jsonPath("$.[1].id").value(userSecond.getId()),
                        MockMvcResultMatchers.jsonPath("$.[1].age").value(userSecond.getAge()),
                        MockMvcResultMatchers.jsonPath("$.[1].name").value(userSecond.getName()),
                        MockMvcResultMatchers.jsonPath("$.[2].name").doesNotExist()

                );
    }

    @Test
    public void getUsersUnique() throws Exception {
        String firstName = UUID.randomUUID().toString();
        String secondName = UUID.randomUUID().toString();

        List<String> usersName = new ArrayList<>();
        usersName.add(firstName);
        usersName.add(secondName);

        when(userRepo.findAllDistinctNamesWithArticlesMoreThen(3))
                .thenReturn(usersName);

        mvc.perform(MockMvcRequestBuilders
                        .get("/users/unique")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.[*]").exists(),
                        MockMvcResultMatchers.jsonPath("$.[0]").value(firstName),
                        MockMvcResultMatchers.jsonPath("$.[1]").value(secondName)
                );
    }

    @Test
    public void createUsers() throws Exception {
        User user = DataProvider.getUser(null);
        UserRequest userRequest = new UserRequest(UUID.randomUUID().toString(), DataProvider.getRandomInt(1, 85));

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        when(userRepo.save(userArgumentCaptor.capture()))
                .thenReturn(user);

        mvc.perform(MockMvcRequestBuilders
                        .post("/users/create")
                        .content(toJson(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        MockMvcResultMatchers.jsonPath("$.id").value(user.getId()),
                        MockMvcResultMatchers.jsonPath("$.age").value(user.getAge()),
                        MockMvcResultMatchers.jsonPath("$.name").value(user.getName())
                );

        assertThat(userArgumentCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(User.builder()
                        .age(userRequest.getAge())
                        .name(userRequest.getName())
                        .build());
    }

    @SneakyThrows
    private String toJson(UserRequest userRequest) {
        return OBJECT_MAPPER.writeValueAsString(userRequest);
    }
}