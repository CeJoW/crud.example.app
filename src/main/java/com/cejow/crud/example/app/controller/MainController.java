package com.cejow.crud.example.app.controller;

import com.cejow.crud.example.app.dao.RoleRepository;
import com.cejow.crud.example.app.model.Role;
import com.cejow.crud.example.app.model.User;
import com.cejow.crud.example.app.model.UserDto;
import com.cejow.crud.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/users")
    public User insertUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/users/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PatchMapping("/users/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/roles")
    public List<Role> getRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    @PostMapping("/registration")
    public String doRegister(@RequestBody User user) {
        return userService.doRegister(user);
    }
}
