package com.cejow.crud.example.app.controller;

import com.cejow.crud.example.app.model.User;
import com.cejow.crud.example.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ModelAndView getLoginForm(@RequestParam(name = "error", required = false) boolean error) {
        ModelAndView modelAndView = new ModelAndView("authentication/login");
        modelAndView.addObject("logInError", error ? "User not found!" : null);
        return modelAndView;
    }

    @GetMapping("/registration")
    public ModelAndView getRegistrationForm(String errorMsg) {
        ModelAndView modelAndView = new ModelAndView("authentication/registration");
        modelAndView.addObject("user", new User());
        modelAndView.addObject("registrationError", errorMsg);
        return modelAndView;
    }

    @PostMapping("/registration")
    public ModelAndView doRegister(@ModelAttribute("user") User user) {
        String str = userService.doRegister(user);

        if (str != null)
            return getRegistrationForm(str);
        else
            return new ModelAndView("redirect:/login");
    }

    @GetMapping("/accessDenied")
    public String getAccessDeniedPage() {
        return "authentication/accessDenied";
    }
}
