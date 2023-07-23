package com.shegami.securityJwt.repositories;

import com.shegami.securityJwt.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByName(String name);
}
