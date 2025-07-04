package com.emp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.emp.entities.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
	

    private final String SECRET_KEY = "mNGkXqDyZfQnD8zMsFnCJRYBtMZCJEM9uygwhKaGzN4=";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                                  .setSigningKey(SECRET_KEY)
                                  .parseClaimsJws(token)
                                  .getBody();
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
    	System.out.println(user);
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(user.getUsername())
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                   .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                   .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
