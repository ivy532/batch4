package com.retail.security;
 
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
 
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
 
@Component
public class JwtUtil {
 
    @Value("${app.jwt.secret}")
    private String secret;
 
    @Value("${app.jwt.expiration}")
    private long expiration;
 
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
 
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
 
    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
 
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
 
    private boolean isTokenExpired(String token) {
        Date exp = Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getExpiration();
        return exp.before(new Date());
    }
}
 