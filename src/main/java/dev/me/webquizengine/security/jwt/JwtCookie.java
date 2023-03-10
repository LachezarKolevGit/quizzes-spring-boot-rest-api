package dev.me.webquizengine.security.jwt;

import dev.me.webquizengine.user.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtCookie {

    private static final Logger logger = LoggerFactory.getLogger(JwtCookie.class);

    @Value("${security.jwt.jwtSecret}")
    private String secret;

    @Value("${security.jwt.jwtExpirationMs}")
    private int jwtExpirationTimeMs;

    @Value("${security.jwt.jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationTimeMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public ResponseCookie generateJwtCookie(User user) { //the cookie is created from the userName and the secret
        String jwt = generateTokenFromUsername(user.getEmail());
        return ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();

    }

    public ResponseCookie getCleanJwtCookie() {  //cleanCookie for the logout function
        return ResponseCookie.from(jwtCookie, null).path("/api").build();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            logger.error("JWT exception occurred: {}", e.getMessage());
        }
        return false;
    }
}
