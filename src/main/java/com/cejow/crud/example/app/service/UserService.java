package com.cejow.crud.example.app.service;

import com.cejow.crud.example.app.config.BCryptEncoderConfig;
import com.cejow.crud.example.app.dao.UserRepository;
import com.cejow.crud.example.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptEncoderConfig bCryptEncoderConfig;

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public String doRegister(User user){
        if (userRepository.findByLoginIs(user.getLogin()).isPresent()) {
            return "User exists!";
        }

        addUser(user);

        return null;
    }

    public void addUser(User user){
        user.setPassword(bCryptEncoderConfig.passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public void deleteUser(long id){
        userRepository.deleteById(id);
    }
}
