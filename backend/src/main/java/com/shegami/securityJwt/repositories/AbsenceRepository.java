package com.shegami.securityJwt.repositories;

import com.shegami.securityJwt.entities.Absence;
import com.shegami.securityJwt.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, String> {
    List<Absence> findAllByUser(AppUser user);
}
