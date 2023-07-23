package com.shegami.securityJwt.controllers;

import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.entities.Role;
import com.shegami.securityJwt.models.RoleToUser;
import com.shegami.securityJwt.services.AccountService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequestMapping(path = "/api/users")
public class AccountController {

    private final AccountService accountService;

    public AccountController(@Lazy AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping("all")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<AppUser> allUsers() {
        return accountService.listUser();
    }

    @GetMapping("id/{id}")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<AppUser> getUserById(@PathVariable("id") String id) {

        AppUser user = accountService.getUserById(id);

        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }

    @GetMapping("username/{username}")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable("username") String username) {

        AppUser user = accountService.getUserByUsername(username);

        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }

    @PostMapping(path = "/add-user")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AppUser> addUser(@RequestBody AppUser appUser) {


        AppUser user = accountService.addNewUser(appUser);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete-user/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) {

        accountService.deleteUser(id);

        return new ResponseEntity<>(Map.of("Message", "User Deleted Successfully"), HttpStatus.OK);
    }

    @PostMapping(path = "/add-role")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Object> addRole(@RequestBody Role role) {

        Role addedRole = accountService.addNewRole(role);

        return new ResponseEntity<>(addedRole, HttpStatus.OK);
    }

    @PostMapping(path = "/add-role-to-user")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Object> addRoleToUser(@RequestBody RoleToUser roleToUser) {

        accountService.addRoleToUser(roleToUser.getUsername(), roleToUser.getRole());

        return new ResponseEntity<>(Map.of("Message", "Role added To user Successfully"), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete-role-from-user")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Object> deleteRoleFromUser(@RequestBody RoleToUser roleToUser) {

        accountService.deleteRoleFromUser(roleToUser.getUsername(), roleToUser.getRole());

        return new ResponseEntity<>(Map.of("Message", "Role deleted To user Successfully"), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete-role/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Object> deleteRole(@PathVariable String id) {

        accountService.deleteRole(id);

        return new ResponseEntity<>(Map.of("Message", "Role Deleted Successfully"), HttpStatus.OK);
    }

}
