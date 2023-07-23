package com.shegami.securityJwt.controllers;


import com.shegami.securityJwt.entities.*;
import com.shegami.securityJwt.models.ApprovalDto;
import com.shegami.securityJwt.models.ApprovalStatus;
import com.shegami.securityJwt.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.shegami.securityJwt.Utils.RequestManagement.resolveToken;

@AllArgsConstructor
@RestController
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequestMapping(path = "/api/approvals")
public class ApprovalController {


    private final ApprovalService approvalService;
    private final AbsenceService absenceService;
    private final JwtDecoder jwtDecoder;
    private final AccountService accountService;
    private final NotificationService notificationService;
    private final ManagerNotificationService managerNotificationService;

    @GetMapping("all")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Object> getAllApprovals() {

        List<Approval> approvalsList = approvalService.listAllApprovals();

        return new ResponseEntity<>(approvalsList, HttpStatus.OK);
    }


    @GetMapping("{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<Object> getApprovalById(@PathVariable("id") String id) {

        Approval approval = approvalService.getApprovalById(id);

        return new ResponseEntity<>(approval, HttpStatus.OK);
    }

    @PutMapping("{id}/approve")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<Object> approvedAbsence(@RequestBody @Valid ApprovalDto approvalDto, @PathVariable("id") String id, HttpServletRequest request) {


        String jwtToken = resolveToken(request);
        Jwt jwt = jwtDecoder.decode(jwtToken);

        AppUser user = accountService.loadUserByUsername(jwt.getSubject());

        Absence absence = absenceService.getAbsenceById(id);

        ApprovalStatus approvalStatus = approvalDto.isApproved() ? ApprovalStatus.APPROVED : ApprovalStatus.DECLINED;

        Approval newApproval = Approval.builder()
                .approved(approvalStatus)
                .manager(user)
                .absence(absence)
                .comments(approvalDto.getComments())
                .build();

        approvalService.approveRequest(newApproval);

        Notification notification = Notification.builder()
                .absence(absence)
                .content("Your Request (" + absence.getTitle() + ") has been " + newApproval.getApproved())
                .date(String.valueOf(new Date()))
                .user(absence.getUser())
                .build();
        notificationService.addNotification(notification);

        ManagerNotification managerNotification = managerNotificationService.findByAbsence(absence);

        managerNotificationService.deleteById(managerNotification.getId());

        return new ResponseEntity<>(Map.of("Message", "Absence Updated", "absence", newApproval), HttpStatus.OK);

    }

    @PutMapping("{id}/approve/edit")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<Object> editApprovedAbsence(@RequestBody @Valid ApprovalDto approvalDto, @PathVariable("id") String id, HttpServletRequest request) {


        String jwtToken = resolveToken(request);
        Jwt jwt = jwtDecoder.decode(jwtToken);

        AppUser user = accountService.loadUserByUsername(jwt.getSubject());

        Absence absence = absenceService.getAbsenceById(id);

        ApprovalStatus approvalStatus = approvalDto.isApproved() ? ApprovalStatus.APPROVED : ApprovalStatus.DECLINED;
        Approval approval = Approval.builder()
                .approved(approvalStatus)
                .manager(user)
                .absence(absence)
                .comments(approvalDto.getComments())
                .build();


        approvalService.updateApproval(approval);

        Notification notification = Notification.builder()
                .absence(absence)
                .content("Your Request (" + absence.getTitle() + ") has been " + approval.getApproved())
                .date(String.valueOf(new Date()))
                .user(absence.getUser())
                .build();
        notificationService.addNotification(notification);


        return new ResponseEntity<>(Map.of("Message", "Absence Updated", "absence", approval), HttpStatus.OK);

    }
}
