package com.cejow.crud.example.app.init;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppInit {

    @Bean
    public Faker faker() {
        return new Faker();
    }
}
