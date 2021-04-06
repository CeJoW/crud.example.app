package com.cejow.crud.example.app.dao;

import com.cejow.crud.example.app.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByLoginIs(String login);
}
