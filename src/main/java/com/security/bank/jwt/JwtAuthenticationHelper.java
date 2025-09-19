package com.security.bank.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtAuthenticationHelper {
	
	private final String secret = "thisisjwtTokenthisisjwtTokenthisisjwtTokenthisisjwtTokenthisisjwtToken";
	private final long JWT_TOKEN_VALIDITY =60*60;

	public String getUsernameFromToken(String token) throws Exception {
		Claims claims = getClaimsFromToken(token);
		return claims.getSubject();
	}
	public Claims getClaimsFromToken(String token) throws Exception {
		try {
			return Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(token).getBody();
		} catch (Exception e) {
			throw new AuthenticationException("InValid token or expired");
		}
		
	}
	public boolean isTokenExpired(String token) throws Exception {
		Claims claims = getClaimsFromToken(token);
		Date expDate = claims.getExpiration();
		return expDate.before(new Date());
	}
	public String generateToken(UserDetails userDetails) {
		Map<String,Object> claims = new HashMap<>();
		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*1000))
		.signWith(new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName()), SignatureAlgorithm.HS512).compact();
	}
}
