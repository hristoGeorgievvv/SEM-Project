package nl.tudelft.sem.sem54.authorizarionsevice.configurations;

import com.auth0.jwt.interfaces.RSAKeyProvider;
import java.security.NoSuchAlgorithmException;
import nl.tudelft.sem.sem54.authorizarionsevice.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private final transient UserDetailsServiceImpl userDetailsService;
    private final transient BCryptPasswordEncoder bcryptPasswordEncoder;
    private final transient RSAKeyProvider keyProvider;

    private final transient Long expirationTime;

    /**
     * Constructor for the websecurity configuration.
     *
     * @param userService The user service used
     * @param bcryptPasswordEncoder The password encoder used
     * @throws NoSuchAlgorithmException When the KeyProvider class throws a NoSuchAlgorithmException
     */
    public WebSecurity(UserDetailsServiceImpl userService,
                       BCryptPasswordEncoder bcryptPasswordEncoder)
            throws NoSuchAlgorithmException {
        this.userDetailsService = userService;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;

        this.expirationTime = (long) 900000;
        String publicKey = "./keys/public_key.der";
        String privateKey = "./keys/private_key.der";

        this.keyProvider = new KeyProviderImp(publicKey, privateKey);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .authorizeRequests()
            .antMatchers("auth/login/**").permitAll()
            .antMatchers(HttpMethod.POST, "/auth/user").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(new AuthenticationFilter(
                    authenticationManager(), expirationTime, keyProvider))
            .addFilter(new AuthorizationFilter(
                    authenticationManager(), keyProvider))
            // this disables session creation on Spring Security
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            //.and().formLogin()//enable form login instead of basic login
            //.and().csrf().ignoringAntMatchers("/h2-console/**")//no CSRF protection to /h2-console
            //.and().headers().frameOptions().sameOrigin()

            .and().csrf().disable(); //allow use of frame to same origin urls
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bcryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}