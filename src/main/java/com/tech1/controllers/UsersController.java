package com.tech1.controllers;

import com.tech1.controllers.request.UserRequest;
import com.tech1.entity.Color;
import com.tech1.entity.User;
import com.tech1.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @GetMapping(params = "color")
    public List<User> getUserByColor(@RequestParam Color color) {
        return userService.getUserByColor(color);
    }

    @GetMapping(params = "age")
    public List<User> getUserByAge(@RequestParam int age){
        return userService.getUserByAge(age);
    }
    @GetMapping("/unique")
    public List<String> getUniqueName(){
        return userService.getUniqueUserName();
    }
    @GetMapping
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    @PostMapping("/create")
    public  void createUser(@RequestBody UserRequest request){
        User user = new User();
        user.setAge(request.getAge());
        user.setName(request.getName());
        userService.createUser(user);
    }
}
