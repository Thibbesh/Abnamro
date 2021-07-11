package com.abnamro.nl.recipes.security.jwt;

import io.jsonwebtoken.*;
import com.abnamro.nl.recipes.service.auth.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JwtUtils its a spring component inorder to
 * <p>generateJwtToken</p>
 * <p>getUserNameFromJwtToken</p>
 * <p>validateJwtToken</p>
 */
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${abnamro.app.jwtSecret}")
    private String jwtSecret;

    @Value("${abnamro.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * generateJwtToken is generate access token of JWT.
     * @param authentication of Authentication Class
     * @return String of JwtToken
     */
    public String generateJwtToken(Authentication authentication) {

        var userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                            .setSubject((userPrincipal.getUsername()))
                            .setIssuedAt(new Date())
                            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                            .signWith(SignatureAlgorithm.HS512, jwtSecret)
                            .compact();
    }

    /**
     * Get username from access token and validate with jwtSecret.
     * @param token accessToken
     * @return user name of logged in user
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                            .setSigningKey(jwtSecret)
                            .parseClaimsJws(token)
                            .getBody()
                            .getSubject();
    }

    /**
     * validate access token with jwtSecret
     * @param authToken accessToken
     * @return true or false
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
