package com.cejow.crud.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping(path = "/main")
    public String getMainPage() {
        return "mainPage";
    }
}
