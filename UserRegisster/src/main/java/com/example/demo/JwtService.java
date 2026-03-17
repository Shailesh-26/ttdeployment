package com.example.demo;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +  60 * 1000))
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
    }
    public void validateToken(String token) {
    	Jwts.parserBuilder()
    	.setSigningKey(secret)
    	.build()
    	.parseClaimsJws(token);
    }
}
