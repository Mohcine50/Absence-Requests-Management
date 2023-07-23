package com.shegami.securityJwt.controllers;

import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.entities.Role;
import com.shegami.securityJwt.exceptions.NotFoundException;
import com.shegami.securityJwt.models.AuthManager;
import com.shegami.securityJwt.models.RegisterAuthManager;
import com.shegami.securityJwt.repositories.AppUserRepository;
import com.shegami.securityJwt.services.AccountService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
@Transactional
public class AuthController {

    private final JwtEncoder jwtEncoder;
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final AccountService accountService;


    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterAuthManager registerAuthManager) {

        Map<String, String> map = new HashMap<>();

        accountService.addNewUser(new AppUser(null, registerAuthManager.getUsername(), registerAuthManager.getEmail(),
                registerAuthManager.getPassword(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));


        map.put("Message", "REGISTER SUCCESSFULLY");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthManager authManger, HttpServletResponse response) {

        Map<String, String> map = new HashMap<>();
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSet;
        String jwtAccessToken;


        AppUser appUser = appUserRepository.findByUsername(authManger.getUsername());
        if (appUser == null) {
            throw new NotFoundException("User Not Found");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authManger.getUsername(), authManger.getPassword()));

            String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

            jwtClaimsSet = JwtClaimsSet.builder()
                    .subject(authentication.getName())
                    .issuedAt(instant)
                    .expiresAt(instant.plus(30, ChronoUnit.MINUTES))
                    .issuer("security-service")
                    .claim("scope", scope)
                    .build();
            jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
            map.put("Message", "Login Successfully");
            map.put("accessToken", jwtAccessToken);
        } catch (AuthenticationException exception) {
            map.put("Message", "Wrong Password");
            map.put("Error", exception.getMessage());
            return new ResponseEntity<>(map, HttpStatus.resolve(401));
        }

        Cookie cookie = new Cookie("authorization_token", jwtAccessToken);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @PostMapping("logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        // Perform logout actions, such as token invalidation or session cleanup
        Map<String, String> map = new HashMap<>();

        map.put("Message", "Logout Successfully");

        return ResponseEntity.ok(map);
    }

}
