package com.zidio.connect.config.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.zidio.connect.config.security.jwt.JwtAuthFilter;
import com.zidio.connect.config.security.jwt.exception.CustomAccessDeniedHandlerImpl;
import com.zidio.connect.enums.AuthorityTypeEnum;

@Configuration
public class SecurityConfig {
	private AuthenticationEntryPoint authenticationEntryPoint;
	private CustomAccessDeniedHandlerImpl customAccessDeniedHandler;
	private JwtAuthFilter jwtAuthFilter;

	public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuthenticationEntryPoint authenticationEntryPoint,
			CustomAccessDeniedHandlerImpl customAccessDeniedHandler) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.customAccessDeniedHandler = customAccessDeniedHandler;
	}

	@Bean
	SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

		// Disabled CSRF
		httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(request -> {
					request.requestMatchers("/api/v1/auth/**", "/error").permitAll()
							// ROLE-based access control
							.requestMatchers("/api/v1/admin/**", "/api/v1/user/**", "/api/v1/role/**")
							.hasRole(AuthorityTypeEnum.ADMIN.toString())

							.requestMatchers("/api/v1/recruiter/**").hasRole(AuthorityTypeEnum.RECRUITER.toString())

							.requestMatchers("/api/v1/student/**").hasRole(AuthorityTypeEnum.STUDENT.toString())

							.requestMatchers("/api/v1/teacher/**").hasRole(AuthorityTypeEnum.TEACHER.toString())

							.anyRequest().authenticated();
				}).exceptionHandling(ex -> {
					ex.authenticationEntryPoint(authenticationEntryPoint);
					ex.accessDeniedHandler(customAccessDeniedHandler);
				}).formLogin(form -> form.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // add custom filter
				.httpBasic(httpBasic -> httpBasic.disable()); // Disable basic auth if using JWT

		return httpSecurity.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:5173")); // your React app
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
}
