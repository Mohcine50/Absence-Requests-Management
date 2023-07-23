package com.shegami.securityJwt.controllers;


import com.shegami.securityJwt.entities.ManagerNotification;
import com.shegami.securityJwt.entities.Notification;
import com.shegami.securityJwt.services.ManagerNotificationService;
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
@RequestMapping(path = "api/managerNotifications")
public class ManagerNotificationController {


    private final ManagerNotificationService managerNotificationService;

    @GetMapping("all")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<Object> getAllNotifications() {

        List<ManagerNotification> notificationList = managerNotificationService.listNotifications();

        return new ResponseEntity<>(notificationList, HttpStatusCode.valueOf(200));

    }

    @DeleteMapping("deleteAll")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<Object> removeAll() {

        managerNotificationService.deleteAllNotification();

        return new ResponseEntity<>(Map.of("Message", "All notification are cleared"), HttpStatus.OK);
    }


}
