package com.tech1;

import com.tech1.entity.Article;
import com.tech1.entity.Color;
import com.tech1.entity.User;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.UUID;

@UtilityClass
public class DataProvider {

    public User getUser(Set<Article> articles) {
        return User.builder()
                .id(getRandomInt(1, Integer.MAX_VALUE))
                .age(getRandomInt(15, 85))
                .name(UUID.randomUUID().toString())
                .articles(articles)
                .build();
    }

    public Article getArticle(User user) {
        return Article.builder()
                .id(getRandomInt(1, Integer.MAX_VALUE))
                .user(user)
                .color(Color.values()[getRandomInt(0, Color.values().length - 1)])
                .text(UUID.randomUUID().toString())
                .build();
    }

    public int getRandomInt(int from, int to) {
        return (int) (Math.random() * (to - from) + from);
    }
}
