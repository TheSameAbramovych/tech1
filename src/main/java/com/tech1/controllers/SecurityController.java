package com.tech1.controllers;

import com.tech1.controllers.request.AuthRequest;
import com.tech1.controllers.response.TokenResponse;
import com.tech1.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;

@RestController
@AllArgsConstructor
@RequestMapping("/security")
public class SecurityController {

    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> auth(@RequestBody AuthRequest request) throws AuthException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getLogin());

        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new AuthException();
        }

        String token = jwtProvider.generateToken(request.getLogin());
        return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);
    }
}
