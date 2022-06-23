package com.tech1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String text;

    @Enumerated(EnumType.STRING)
    private Color color;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "author_id")
    private User user;

}

