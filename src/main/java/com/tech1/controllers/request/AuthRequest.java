package com.tech1.controllers.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRequest {
    private String login;
    private String password;
}
