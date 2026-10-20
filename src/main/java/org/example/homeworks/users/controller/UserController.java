package org.example.homeworks.users.controller;

import org.example.homeworks.users.dto.UserDto;
import org.example.homeworks.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//http://localhost:5438/suroegina/users
//http://localhost:5438/suroegina/users/1

@RestController
@RequestMapping("/suroegina/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({ "", "/" })
    public List<UserDto> getAllUsers() {
        return userService.getAllUsersDto();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserByIdDto(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}