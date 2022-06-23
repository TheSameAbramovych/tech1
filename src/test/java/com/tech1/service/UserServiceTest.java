package com.tech1.service;

import com.tech1.DataProvider;
import com.tech1.controllers.request.UserRequest;
import com.tech1.entity.Article;
import com.tech1.entity.Color;
import com.tech1.entity.User;
import com.tech1.exception.NotFoundUserException;
import com.tech1.repository.ArticleRepo;
import com.tech1.repository.UserRepo;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private ArticleRepo articleRepo;

    @InjectMocks
    private UserService userService;

    @Test
    public void getAllUser() {
        List<User> users = Collections.singletonList(DataProvider.getUser(null));

        when(userRepo.findAll())
                .thenReturn(users);

        List<User> result = userService.getAllUser();

        assertThat(users)
                .isEqualTo(result);
    }

    @Test
    public void getUniqueUserName() {
        List<String> names = Collections.singletonList(UUID.randomUUID().toString());

        when(userRepo.findAllDistinctNamesWithArticlesMoreThen(3))
                .thenReturn(names);

        List<String> result = userService.getUniqueUserName();

        assertThat(names)
                .isEqualTo(result);
    }

    @Test
    public void findByUserId() {
        User user = DataProvider.getUser(null);

        when(userRepo.findById(user.getId()))
                .thenReturn(Optional.of(user));

        User result = userService.findByUserId(user.getId());

        assertThat(user)
                .isEqualTo(result);
    }

    @Test
    public void findByUserId_userNotFound() {
        User user = DataProvider.getUser(null);

        when(userRepo.findById(user.getId()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundUserException.class, () -> userService.findByUserId(user.getId()));
    }

    @Test
    public void getUserByAge() {
        List<User> users = new ArrayList<>();
        users.add(DataProvider.getUser(null));
        users.add(DataProvider.getUser(null));
        users.add(DataProvider.getUser(null));

        when(userRepo.findAllByAgeGreaterThanEqual(3))
                .thenReturn(users);

        List<User> result = userService.getUserByAge(3);

        assertThat(users)
                .isEqualTo(result);
    }

    @Test
    public void getUserByColor() {
        List<Article> articles = new ArrayList<>();
        User user = DataProvider.getUser(null);
        articles.add(DataProvider.getArticle(user));
        articles.add(DataProvider.getArticle(user));
        articles.add(DataProvider.getArticle(DataProvider.getUser(null)));

        when(articleRepo.findAllByColor(Color.RED))
                .thenReturn(articles);

        List<User> result = userService.getUserByColor(Color.RED);

        assertThat(articles.stream()
                .map(Article::getUser)
                .distinct()
                .collect(Collectors.toList()))
                .isEqualTo(result);
    }

    @Test
    public void createUser() {
        User user = DataProvider.getUser(null);
        UserRequest userRequest =
                new UserRequest(UUID.randomUUID().toString(), DataProvider.getRandomInt(15, 85));

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepo.save(userArgumentCaptor.capture()))
                .thenReturn(user);

        User result = userService.createUser(userRequest);

        assertAll(
                () -> AssertionsForClassTypes.assertThat(userArgumentCaptor.getValue())
                        .usingRecursiveComparison()
                        .isEqualTo(User.builder()
                                .age(userRequest.getAge())
                                .name(userRequest.getName())
                                .build()),
                () -> AssertionsForClassTypes.assertThat(user)
                        .usingRecursiveComparison()
                        .isEqualTo(result)
        );
    }
}