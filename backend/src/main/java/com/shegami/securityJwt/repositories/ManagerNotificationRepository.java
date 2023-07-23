package com.shegami.securityJwt.repositories;

import com.shegami.securityJwt.entities.Absence;
import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.entities.ManagerNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerNotificationRepository extends JpaRepository<ManagerNotification, String> {
    ManagerNotification findByAbsence(Absence absence);
}
