package com.shegami.securityJwt.services;

import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.entities.Role;
import com.shegami.securityJwt.exceptions.ApiRequestException;
import com.shegami.securityJwt.exceptions.NotFoundException;
import com.shegami.securityJwt.repositories.AppUserRepository;
import com.shegami.securityJwt.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {


        AppUser user = appUserRepository.findByUsername(appUser.getUsername());
        if (user != null) {
            throw new ApiRequestException("Username Already Exist please try other one");
        }

        Role role = roleRepository.findByName("USER");

        AppUser newUser = AppUser.builder()
                .username(appUser.getUsername())
                .password(passwordEncoder.encode(appUser.getPassword()))
                .email(appUser.getEmail())
                .roles(List.of(role))
                .build();


        appUserRepository.save(newUser);


        return newUser;
    }

    @Override
    public AppUser getUserById(String id) {
        AppUser user = appUserRepository.findById(id).orElse(null);

        if (user == null) {
            throw new NotFoundException("No User Found With ID: " + id);
        }

        return user;
    }

    @Override
    public AppUser getUserByUsername(String username) {
        AppUser user = appUserRepository.findByUsername(username);

        if (user == null) {
            throw new NotFoundException("No User Found With Username: " + username);
        }

        return user;
    }

    @Override
    public Role addNewRole(Role role) {

        Role role1 = roleRepository.findByName(role.getName());
        if (role1 != null) {
            throw new ApiRequestException("Role Already exist");
        }

        Role newRole = Role.builder()
                .name(role.getName())
                .build();


        roleRepository.save(newRole);

        return newRole;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {

        AppUser user = appUserRepository.findByUsername(username);
        if (user == null) throw new NotFoundException("User Not Found");
        Role role = roleRepository.findByName(roleName);
        if (role == null) throw new NotFoundException("Role Not Found");


        Collection<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new ArrayList<>(List.of(role));
        } else {
            roles.add(role);
        }

    }

    @Override
    public void deleteRoleFromUser(String username, String roleName) {
        AppUser user = appUserRepository.findByUsername(username);
        if (user == null) throw new NotFoundException("User Not Found");
        Role role = roleRepository.findByName(roleName);
        if (role == null) throw new NotFoundException("Role Not Found");


        Collection<Role> roles = user.getRoles();
        roles.remove(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUser() {
        return appUserRepository.findAll();
    }

    @Override
    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }

    @Override
    public void deleteUser(String id) {
        appUserRepository.deleteById(id);
    }


}
