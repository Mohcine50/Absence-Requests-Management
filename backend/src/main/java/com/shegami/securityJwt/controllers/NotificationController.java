package com.shegami.securityJwt.controllers;


import com.shegami.securityJwt.entities.Notification;
import com.shegami.securityJwt.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequestMapping(path = "api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("{username}/all")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<Object> getNotificationsForUser(@PathVariable("username") String username) {

        List<Notification> notificationList = notificationService.listNotificationsByUser(username);

        return new ResponseEntity<>(notificationList, HttpStatusCode.valueOf(200));

    }

    @DeleteMapping("{username}/deleteAll")
    public ResponseEntity<Object> removeAll(@PathVariable("username") String username) {

        notificationService.deleteAllNotification(username);

        return new ResponseEntity<>(Map.of("Message", "All notification are cleared"), HttpStatus.OK);
    }


}
