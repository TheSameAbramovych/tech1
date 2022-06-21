package com.tech1.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserRequest {
    private final String name;
    private final int age;
}
