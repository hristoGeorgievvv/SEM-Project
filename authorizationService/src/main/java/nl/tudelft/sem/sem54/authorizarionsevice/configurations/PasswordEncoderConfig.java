package nl.tudelft.sem.sem54.authorizarionsevice.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
    /**
     * Defines bean for the password encoder that will be used in this microservice.
     *
     * @return BCryptPasswordEncoder object
     */
    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
