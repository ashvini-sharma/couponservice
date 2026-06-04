package com.firstbeat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class CouponServiceCnfiguration {
	
	@Bean
	AuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
	    provider.setPasswordEncoder(encoder());
	    return provider;
	}
	
	@Bean
	UserDetailsService userDetailsService() {
		UserDetails ashvini = User.withUsername("ashvini").password(encoder().encode("admin123")).roles("ADMIN-HIGH", "USER-LOW").build();
		UserDetails nikhil = User.withUsername("nikhil").password(encoder().encode("user123")).roles("USER-LOW").build();
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager(ashvini, nikhil);
		return manager;
	}
	
	@Bean
	BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean  // ashvini/admin123 (ADMIN) has all the access but nikhil/user123 (USER) has access to only GET /hello/shuru 
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		httpSecurity.csrf(csrf -> csrf.disable())
		            //.httpBasic(Customizer.withDefaults())
		            .authorizeHttpRequests(T -> 
		                   T.requestMatchers(HttpMethod.GET, "/couponapi/coupons/*", "/couponapi/coupons").hasAnyRole("ADMIN-HIGH")
		                    .requestMatchers(HttpMethod.POST, "/hello/faltu", "/check/*").hasAnyRole("ADMIN-HIGH")
		                    .requestMatchers("/couponapi/login/**").permitAll()
		            		);
		
		httpSecurity.securityContext((securityContext) -> securityContext.requireExplicitSave(true));
		return httpSecurity.build();
	}
	
	@Bean
	SecurityContextRepository securityContextRepository() {
		return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
	}
	
	
	
	/*
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .anyRequest().authenticated()
	        )
	        .httpBasic(Customizer.withDefaults());

	    return http.build();
	}*/
	
}
