package com.shegami.securityJwt.controllers;


import com.shegami.securityJwt.entities.Absence;
import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.entities.ManagerNotification;
import com.shegami.securityJwt.models.AbsenceDto;
import com.shegami.securityJwt.services.AbsenceService;
import com.shegami.securityJwt.services.AccountService;
import com.shegami.securityJwt.services.ManagerNotificationService;
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
@RequestMapping(path = "/api/absences")
public class AbsenceController {

    private final AbsenceService absenceService;
    private final JwtDecoder jwtDecoder;
    private final AccountService accountService;
    private final ManagerNotificationService managerNotificationService;

    @GetMapping("all")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_MANAGER')")
    public ResponseEntity<Object> getAllAbsences() {

        List<Absence> absencesList = absenceService.listAllAbsences();

        return new ResponseEntity<>(absencesList, HttpStatus.OK);
    }

    @GetMapping("{username}/absences/all")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<Object> getAllAbsencesByUser(@PathVariable("username") String username) {


        List<Absence> absencesList = absenceService.listByUser(username);

        return new ResponseEntity<>(absencesList, HttpStatus.OK);
    }

    @PostMapping("new")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<Object> addNewAbsence(@RequestBody @Valid AbsenceDto absenceDto, HttpServletRequest request) {

        String jwtToken = resolveToken(request);
        Jwt jwt = jwtDecoder.decode(jwtToken);

        AppUser user = accountService.loadUserByUsername(jwt.getSubject());

        Absence absence = Absence.builder()
                .id(null)
                .title(absenceDto.getTitle())
                .startDate(absenceDto.getStartDate())
                .endDate(absenceDto.getEndDate())
                .reason(absenceDto.getReason())
                .user(user)
                .createdDate(String.valueOf(new Date()))
                .build();

        Absence addedAbsence = absenceService.addNewAbsence(absence);

        ManagerNotification managerNotification = ManagerNotification
                .builder()
                .absence(absence)
                .date(String.valueOf(new Date()))
                .user(user)
                .content(user.getUsername() + " submitted an Absence Request (" + absence.getTitle() + " )")
                .build();

        managerNotificationService.addNotification(managerNotification);

        return new ResponseEntity<>(Map.of("Message", "Absence Added Successfully", "Absence", addedAbsence), HttpStatus.OK);
    }

    @GetMapping("{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<Object> getAbsenceById(@PathVariable("id") String id) {

        Absence absence = absenceService.getAbsenceById(id);

        return new ResponseEntity<>(absence, HttpStatus.OK);
    }

    @PutMapping("{id}/edit")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<Object> editAbsence(@RequestBody AbsenceDto absenceDto, @PathVariable("id") String id) {

        Absence absence = absenceService.getAbsenceById(id);
        absence.setTitle(absenceDto.getTitle());
        absence.setStartDate(absenceDto.getStartDate());
        absence.setEndDate(absenceDto.getEndDate());
        absence.setReason(absenceDto.getReason());

        Absence updateAbsence = absenceService.updateAbsence(absence);

        return new ResponseEntity<>(updateAbsence, HttpStatus.OK);
    }


    @DeleteMapping("{id}/delete")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<Object> deleteAbsence(@PathVariable("id") String id) {

        absenceService.deleteAbsence(id);

        return new ResponseEntity<>(Map.of("Message", "Absence Deleted Successfully"), HttpStatus.OK);
    }


}
