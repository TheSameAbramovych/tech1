package com.tech1.controllers.request;

import com.tech1.entity.Color;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ArticleRequest {
    private final String text;
    private final Color color;
    private final Long userId;
}
