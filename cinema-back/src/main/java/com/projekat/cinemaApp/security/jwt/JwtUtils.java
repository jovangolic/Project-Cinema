package com.projekat.cinemaApp.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.projekat.cinemaApp.security.user.CinemaUserDetails;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}")
    private int jwtExpirationMs;
    
    public String generateJwtTokenForUser (Authentication authentication) {
    	CinemaUserDetails userPrincipal = (CinemaUserDetails)authentication.getPrincipal();
    	List<String> roles = userPrincipal.getAuthorities()
    			.stream()
    			.map(GrantedAuthority::getAuthority).toList();
    	return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+jwtExpirationMs * 2))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	//ekstrahovanje korisnickog imena iz tokena
	public String getUserNameFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key())
				.build()
				.parseClaimsJws(token)
				.getBody().getSubject();
	}
	
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
			return true;
		}
		catch(MalformedJwtException e) {
			logger.error("Invalid jwt token : {}" , e.getMessage());
		}
		catch(ExpiredJwtException e) {
			logger.error("Expired token : {}" , e.getMessage());
		}
		catch(UnsupportedJwtException e) {
			logger.error("This token is not supported : {}" , e.getMessage());
		}
		catch(IllegalArgumentException e) {
			logger.error("No claims found : {}", e.getMessage());
		}
		return false;
	}

}
