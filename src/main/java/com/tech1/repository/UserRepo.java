package com.tech1.repository;

import com.tech1.entity.Color;
import com.tech1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findAllByArticles_Color(Color color);

    List<User> findAllByAgeGreaterThanEqual(int age);

    @Query(value = "SELECT DISTINCT u.name FROM User u " +
            "JOIN Article a ON a.user = u " +
            "GROUP BY a.user " +
            "HAVING COUNT(a.id) >= :articlesCount")
    List<String> findAllDistinctNamesWithArticlesMoreThen(@Param("articlesCount") long articlesCount);
}
