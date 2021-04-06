package com.cejow.crud.example.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeExclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column
    @HashCodeExclude
    private String password;

    @Column
    private boolean active;

    @OneToMany(targetEntity = Role.class, mappedBy = "roleId", fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String password, List<Role> roles, boolean active) {
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.active = active;
    }
}
