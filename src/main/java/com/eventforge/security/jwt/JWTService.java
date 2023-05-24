package com.eventforge.security.jwt;

import com.eventforge.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final UserRepository userRepository;
    @Value("${spring.jwt.secret}")
    private String JWT_SECRET;
    @Value("${spring.jwt.jwtExpirationTime}")
    private int JWT_EXPIRATION_TIME;
    public String getGeneratedToken(String username) {
        Map<String , Object> claims = new HashMap<>();
        return generateTokenForUser(claims , username);
    }

    private String generateTokenForUser(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+JWT_EXPIRATION_TIME))
                .signWith(getSignKey() , SignatureAlgorithm.ES256).compact();
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsernameFromToken(String theToken){
        return extractClaim(theToken , Claims :: getSubject);
    }

    public Date extractExpirationTimeFromToken(String theToken){
        return extractClaim(theToken , Claims:: getExpiration);
    }

    public Boolean validateToken(String theToken , UserDetails userDetails){
        final String username = extractUsernameFromToken(theToken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(theToken));
    }

    private <T> T extractClaim(String theToken , Function<Claims , T> claimsResolver){
        final Claims claims = extractAllClaims(theToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String theToken){
        return extractExpirationTimeFromToken(theToken).before(new Date());
    }
}
