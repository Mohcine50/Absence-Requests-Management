package com.shegami.securityJwt.services;

import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.entities.Notification;
import com.shegami.securityJwt.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final AccountService accountService;

    @Override
    public Notification addNotification(Notification notification) {
        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public void deleteAllNotification(String username) {

        AppUser user = accountService.getUserByUsername(username);
        notificationRepository.deleteAllByUser(user);
    }

    @Override
    public List<Notification> listNotificationsByUser(String username) {

        AppUser user = accountService.getUserByUsername(username);

        return notificationRepository.findAllByUser(user);
    }
}
