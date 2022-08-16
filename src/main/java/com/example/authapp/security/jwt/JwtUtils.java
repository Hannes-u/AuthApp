package com.example.authapp.security.jwt;

import com.example.authapp.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

//Klasse für JWT Generierung und Validierung
@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  //Generierung von secret
  private final Key secret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

  @Value("${authApp.jwtExpirationMs}")
  private int jwtExpirationMs;

  public  int getJwtExpirations(){
    return jwtExpirationMs;
  }

  //Methode um JWT zu generieren
  public String generateJwtToken(String username,String userFingerprint) throws UnsupportedEncodingException, NoSuchAlgorithmException {

      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] userFingerprintDigest = digest.digest(userFingerprint.getBytes("utf-8"));
      String userFingerprintHash = DatatypeConverter.printHexBinary(userFingerprintDigest);

    return Jwts.builder()
            .claim("userFingerprint",userFingerprintHash)
            .setSubject((username))
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(secret)
            .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody().getSubject();
  }

  //Methode um JWT zu validieren
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(secret)
              .build().parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
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
