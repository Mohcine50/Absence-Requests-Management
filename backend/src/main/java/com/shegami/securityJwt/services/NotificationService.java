package com.shegami.securityJwt.services;

import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.entities.Notification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface NotificationService {

    Notification addNotification(Notification notification);

    void deleteAllNotification(String username);

    List<Notification> listNotificationsByUser(String username);

}
