package com.cejow.crud.example.app.dao;

import com.cejow.crud.example.app.model.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRoleNameIs(String roleName);
}
