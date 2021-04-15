package com.cejow.crud.example.app.model;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    long id;
    String login;
    String password;
    List<Role> roles;
    long vkId;
    String imgURL;

    public UserDto(long id, String login, String password, List<Role> roles, Long vkId) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.vkId = vkId;
    }
}
