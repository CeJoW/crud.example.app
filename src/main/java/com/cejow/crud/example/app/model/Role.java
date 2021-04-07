package com.cejow.crud.example.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;

    @Column(unique = true, nullable = false)
    private String roleName;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    private List<User> usersList = new ArrayList<>();

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
