package com.zidio.connect.config.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.zidio.connect.config.security.jwt.exception.JwtValidationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtHelper{

	private static final String SECRET_KEY = "isarbvoieaunirousiorfnisamrcoiubiruuioeruewionfnfsniviobuvdisnuvoidns";

	private static final Long JWT_TOKEN_VALIDITY_IN_MS = 5 * 60 * 60 * 1000l;
	private static final String ISSUER = "Zidio Connect";
	private static final String AUTHORIZATION_HEADER_STRING = "Authorization";
	private static final String TOKEN_BEARER_PREFIX = "Bearer ";

	public String getJwtTokenFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER_STRING);
		if (bearerToken != null && bearerToken.startsWith(TOKEN_BEARER_PREFIX)) {
			return bearerToken.replace(TOKEN_BEARER_PREFIX, "");
		}
		return null;
	}

	public String generateTokenFromUsername(UserDetails user) {
		if (user == null) {
			throw new NullPointerException("Error generating access token, username is null");
		}
		String email = user.getUsername();
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder().claims(claims).subject(email).issuer(ISSUER).issuedAt(new Date())
				.expiration(new Date((new Date()).getTime() + JWT_TOKEN_VALIDITY_IN_MS)).signWith(getSigningKey())
				.compact();
	}

	private SecretKey getSigningKey() {
		byte[] decode = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(decode);
	}

	public String getUsernameFromJwtToken(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
	}

	public Claims validateJwtToken(String authToken){
		try {
			return Jwts.parser().verifyWith(this.getSigningKey()).build().parseSignedClaims(authToken).getPayload();
		} catch (ExpiredJwtException e) {
			throw new JwtValidationException("JWT token is expired!!");
		} catch (UnsupportedJwtException e) {
			throw new JwtValidationException("JWT token is unsupported!!");
		} catch (MalformedJwtException e) {
			throw new JwtValidationException("Invalid JWT token!!");
		} catch (SignatureException e) {
			throw new JwtValidationException("Invalid signature!!");
		} catch (IllegalArgumentException e) {
			throw new JwtValidationException("Token is empty or null!!");
		}
	}

}
