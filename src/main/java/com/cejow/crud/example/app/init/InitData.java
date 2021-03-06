package com.cejow.crud.example.app.init;

import com.cejow.crud.example.app.config.BCryptEncoderConfig;
import com.cejow.crud.example.app.dao.RoleRepository;
import com.cejow.crud.example.app.dao.UserRepository;
import com.cejow.crud.example.app.model.Role;
import com.cejow.crud.example.app.model.User;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InitData {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptEncoderConfig bCryptEncoderConfig;

    @Autowired
    private Faker faker;

    @PostConstruct
    public void initUsers() {
        Role adminRole = new Role("ROLE_ADMIN");
        roleRepository.save(adminRole);
        Role clientRole = new Role("ROLE_USER");
        roleRepository.save(clientRole);
        List<Role> roles = new ArrayList<>();
        roles.add(adminRole);
        roles.add(clientRole);
        User admin = new User("admin", bCryptEncoderConfig.passwordEncoder().encode("123"), roles);
        admin.setVkId(38108421L);

        userRepository.save(admin);

        userRepository.save(new User("client", bCryptEncoderConfig.passwordEncoder().encode("123"), Collections.singletonList(clientRole)));

        for (int i = 0; i < 5; i++) {
            User user = new User(faker.superhero().name(), bCryptEncoderConfig.passwordEncoder().encode(faker.animal().name()));
            userRepository.save(user);
        }
    }
}
