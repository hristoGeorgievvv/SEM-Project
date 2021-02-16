package nl.tudelft.sem.sem54.mainservice.config;

import java.security.NoSuchAlgorithmException;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import nl.tudelft.sem.sem54.mainservice.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/*
Sources:
https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
https://www.freecodecamp.org/news/how-to-setup-jwt-authorization-and-authentication-in-spring/
https://www.tutorialspoint.com/spring_boot/spring_boot_oauth2_with_jwt.htm
https://www.toptal.com/spring/spring-boot-oauth2-jwt-rest-protection
*/

@Component
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final transient UserDetailsServiceImpl userService;
    private final transient RSAKeyProvider keyProvider;

    private transient String publicKey;

    /**
     * Constructor for the websecurity configuration.
     *
     * @param userService The user service used
     * @throws NoSuchAlgorithmException When the KeyProvider class throws a NoSuchAlgorithmException
     */
    public WebSecurity(UserDetailsServiceImpl userService) {
        this.userService = userService;
        this.publicKey = "./keys/public_key.der";
        this.keyProvider = new KeyProviderImp(this.publicKey);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/main/login/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthorizationFilter(authenticationManager(), keyProvider))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //allow use of frame to same origin urls
                .and().csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}