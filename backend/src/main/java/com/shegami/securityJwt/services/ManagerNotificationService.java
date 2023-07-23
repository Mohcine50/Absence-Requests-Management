package com.shegami.securityJwt.services;

import com.shegami.securityJwt.entities.Absence;
import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.entities.ManagerNotification;
import com.shegami.securityJwt.entities.Notification;

import java.util.List;

public interface ManagerNotificationService {

    ManagerNotification addNotification(ManagerNotification managerNotification);

    void deleteAllNotification();

    List<ManagerNotification> listNotifications();

    ManagerNotification findByAbsence(Absence absence);

    void deleteById(String id);
}
