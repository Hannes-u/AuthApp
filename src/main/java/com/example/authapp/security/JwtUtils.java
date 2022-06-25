package com.example.authapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private static final String ROLES_CLAIM = "roles";

    @Value("${authApp.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${authApp.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateAccessToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .claim(ROLES_CLAIM, roles)
                .signWith(key)
                .compact();
    }

    public String generateRefreshTokenFromUser(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(key)
                .compact();
    }


    public Jws<Claims> parseJwtToken(String authToken) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
    }

    public String unescapeToken(String token) {
        // if there is a quotation mark at the beginning of the token
        if (token.startsWith("\"")) {
            token = token.substring(1);
        }
        // if there is a quotation mark at the end of the token
        if (token.endsWith("\"")) {
            token = token.substring(0, token.length() - 1);
        }
        return token;
    }

}
