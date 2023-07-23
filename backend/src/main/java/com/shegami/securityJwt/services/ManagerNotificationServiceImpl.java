package com.shegami.securityJwt.services;

import com.shegami.securityJwt.entities.Absence;
import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.entities.ManagerNotification;
import com.shegami.securityJwt.repositories.ManagerNotificationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ManagerNotificationServiceImpl implements ManagerNotificationService {

    private final ManagerNotificationRepository managerNotificationRepository;

    @Override
    public ManagerNotification addNotification(ManagerNotification managerNotification) {

        managerNotificationRepository.save(managerNotification);

        return managerNotification;
    }

    @Override
    public void deleteAllNotification() {
        managerNotificationRepository.deleteAll();
    }

    @Override
    public List<ManagerNotification> listNotifications() {
        return managerNotificationRepository.findAll();
    }

    @Override
    public ManagerNotification findByAbsence(Absence absence) {
        return managerNotificationRepository.findByAbsence(absence);
    }

    @Override
    public void deleteById(String id) {
        managerNotificationRepository.deleteById(id);
    }


}
