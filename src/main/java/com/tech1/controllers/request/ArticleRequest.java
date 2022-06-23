package com.tech1.controllers.request;

import com.tech1.entity.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequest {
    private String text;
    private Color color;
    private Long userId;
}
