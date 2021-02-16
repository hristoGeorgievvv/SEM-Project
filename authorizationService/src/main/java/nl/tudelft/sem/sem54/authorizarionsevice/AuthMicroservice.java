package nl.tudelft.sem.sem54.authorizarionsevice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
@EnableConfigurationProperties
public class AuthMicroservice {
    /**
     * Main spring boot application, starts the auth microservice.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthMicroservice.class, args);
    }
}
