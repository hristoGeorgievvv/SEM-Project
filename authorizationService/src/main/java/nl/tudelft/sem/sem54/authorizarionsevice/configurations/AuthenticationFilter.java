package nl.tudelft.sem.sem54.authorizarionsevice.configurations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashSet;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.tudelft.sem.sem54.authorizarionsevice.entities.UserEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*
Sources:
https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
https://www.freecodecamp.org/news/how-to-setup-jwt-authorization-and-authentication-in-spring/
https://www.tutorialspoint.com/spring_boot/spring_boot_oauth2_with_jwt.htm
https://www.toptal.com/spring/spring-boot-oauth2-jwt-rest-protection
*/
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final transient AuthenticationManager authenticationManager;
    private final transient RSAKeyProvider keyProvider;
    private final transient long expirationTime;

    /**
     * Initialize the authentication filter.
     * This class processes a sign in; makes and gives a JWT token.
     *
     * @param authenticationManager the authenticationManager
     * @param expirationTime        the expiring time of the jwt
     */
    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                Long expirationTime, RSAKeyProvider kp) {
        this.authenticationManager = authenticationManager;
        this.expirationTime = expirationTime;

        keyProvider = kp;

        setFilterProcessesUrl("/auth/login");
    }

    /**
     * This checks if an attempt to signing is from a legitimate user.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res)
        throws AuthenticationException {
        try {
            UserEntity userCredentials = new ObjectMapper()
                .readValue(req.getInputStream(), UserEntity.class);

            String user = userCredentials.getUsername();
            String pass = userCredentials.getPassword();

            UsernamePasswordAuthenticationToken credentialAuthToken =
                new UsernamePasswordAuthenticationToken(user, pass, new HashSet<>());

            return authenticationManager.authenticate(credentialAuthToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * When a sign in is ok, this generates a jwt token and puts in the response.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {

        Date expiresAt = new Date(System.currentTimeMillis() + expirationTime);

        String user = ((User) auth.getPrincipal()).getUsername();

        String token = JWT.create()
            .withExpiresAt(expiresAt)
            .withSubject(user)
            .sign(Algorithm.RSA256(keyProvider));

        res.getWriter().write(token);
        res.getWriter().flush();
    }
}
