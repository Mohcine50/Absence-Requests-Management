package com.shegami.securityJwt;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.shegami.securityJwt.config.RsaKeysConfig;
import com.shegami.securityJwt.entities.AppUser;
import com.shegami.securityJwt.entities.Role;
import com.shegami.securityJwt.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@SpringBootApplication
@AllArgsConstructor
@EnableConfigurationProperties(RsaKeysConfig.class)
public class SecurityJwtApplication {

    private RsaKeysConfig rsaKeysConfig;

    public static void main(String[] args) {
        SpringApplication.run(SecurityJwtApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder JwtEncoder() {

        JWK jwk = new RSAKey.Builder(rsaKeysConfig.publicKey()).privateKey(rsaKeysConfig.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwkSource);

    }

    @Bean
    public JwtDecoder JwtDecoder() {

        return NimbusJwtDecoder.withPublicKey(rsaKeysConfig.publicKey()).build();
    }


    CommandLineRunner commandLineRunner(AccountService accountService) {

        return args -> {

            accountService.addNewRole(new Role(null, "ADMIN"));
            accountService.addNewRole(new Role(null, "USER"));
            accountService.addNewRole(new Role(null, "MANAGER"));

            accountService.addNewUser(new AppUser(null, "admin", "admin@gmail.com", "123456789", null, null, null, null));
            accountService.addNewUser(new AppUser(null, "user", "user@gmail.com", "123456789", null, null, null, null));
            accountService.addNewUser(new AppUser(null, "manager", "manager@gmail.com", "123456789", null, null, null, null));


            accountService.addRoleToUser("admin", "ADMIN");
            //accountService.addRoleToUser("admin", "USER");
            accountService.addRoleToUser("admin", "MANAGER");
            //accountService.addRoleToUser("user", "USER");
            accountService.addRoleToUser("manager", "MANAGER");
            //accountService.addRoleToUser("manager", "USER");

        };
    }
}
