package nl.tudelft.sem.sem54.fridge.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;

/*
Sources:
https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
https://www.freecodecamp.org/news/how-to-setup-jwt-authorization-and-authentication-in-spring/
https://www.tutorialspoint.com/spring_boot/spring_boot_oauth2_with_jwt.htm
https://www.toptal.com/spring/spring-boot-oauth2-jwt-rest-protection
*/
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final transient RSAKeyProvider keyProvider;

    /**
     * Initialize the authorization filter.
     * This class processes a request by getting the token, and verifying it.
     *
     * @param authManager the authManager
     */
    public AuthorizationFilter(AuthenticationManager authManager, RSAKeyProvider kp) {
        super(authManager);

        keyProvider = kp;
    }

    /**
     * Extract the jwt token from a request, verify it and set Authentication of the request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.replace("Bearer ", "");
            Optional<String> username = verifyToken(token);

            username.ifPresent(s ->
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(s,
                                    null, new HashSet<>())));
        }
        chain.doFilter(req, res);
    }

    /**
     * Get the username of a token.
     *
     * @param token the token to process
     * @return Optional username. Empty if token is invalid.
     */
    private Optional<String> verifyToken(String token) {
        String username = JWT.require(Algorithm.RSA256(keyProvider))
                .build()
                .verify(token)
                .getSubject();
        return Optional.ofNullable(username);
    }
}