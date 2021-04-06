package com.cejow.crud.example.app.controller;

import com.cejow.crud.example.app.model.User;
import com.cejow.crud.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView getAllUsers() {
        ModelAndView modelAndView = new ModelAndView("/users/users");
        modelAndView.addObject("users", userService.getUsers());
        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView newUserForm() {
        ModelAndView modelAndView = new ModelAndView("/users/new");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView insertUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return getAllUsers();
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editUserForm(@PathVariable("id") long id) {
        ModelAndView modelAndView = new ModelAndView("/users/edit");
        modelAndView.addObject("user", userService.getUser(id));
        return modelAndView;
    }

    @PostMapping("/{id}/edit")
    public ModelAndView updateUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return getAllUsers();
    }

    @PostMapping("/{id}/delete")
    public ModelAndView deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return getAllUsers();
    }
}
