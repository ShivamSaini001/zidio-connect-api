package com.zidio.connect.config.security.jwt;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zidio.connect.config.security.jwt.exception.JwtValidationException;
import com.zidio.connect.exception.CustomException;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private JwtHelper jwtHelper;
	private UserDetailsService userDetailsService;

	@Lazy
	public JwtAuthFilter(JwtHelper jwtHelper, UserDetailsService userDetailsService) {
		this.jwtHelper = jwtHelper;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		return path.startsWith("/api/v1/auth"); // Skip JWT check for /api/v1/auth
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// get Jwt token from header.
		String jwtToken = jwtHelper.getJwtTokenFromHeader(request);
		if (jwtToken != null) {
			try {
				Claims jwtTokenClaims = jwtHelper.validateJwtToken(jwtToken);
				if (jwtTokenClaims != null) {
					String email = jwtHelper.getUsernameFromJwtToken(jwtToken);
					UserDetails user = userDetailsService.loadUserByUsername(email);

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
							null, user.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (JwtValidationException e) {
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.setContentType("application/json");
				Map<String, Object> body = new HashMap<>();
				body.put("message", e.getMessage());
				body.put("timestamp", LocalDateTime.now().toString());
				body.put("status", HttpStatus.UNAUTHORIZED.value());
				new ObjectMapper().writeValue(response.getOutputStream(), body);
			}
		} else {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json");
			Map<String, Object> body = new HashMap<>();
			body.put("message", "Please provide Jwt Token!!");
			body.put("timestamp", LocalDateTime.now().toString());
			body.put("status", HttpStatus.UNAUTHORIZED.value());
			new ObjectMapper().writeValue(response.getOutputStream(), body);
		}
		filterChain.doFilter(request, response);
	}

}
