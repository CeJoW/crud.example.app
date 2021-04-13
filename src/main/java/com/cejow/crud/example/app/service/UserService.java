package com.cejow.crud.example.app.service;

import com.cejow.crud.example.app.config.BCryptEncoderConfig;
import com.cejow.crud.example.app.dao.UserRepository;
import com.cejow.crud.example.app.exceptions.UsersNotFoundException;
import com.cejow.crud.example.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptEncoderConfig bCryptEncoderConfig;

    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsersNotFoundException(id));
    }

    public User addUser(User user) {
        user.setPassword(bCryptEncoderConfig.passwordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public String doRegister(User user) {
        if (userRepository.findByLoginIs(user.getLogin()).isPresent()) {
            return "User exists!";
        }
        addUser(user);
        return "success";
    }
}
