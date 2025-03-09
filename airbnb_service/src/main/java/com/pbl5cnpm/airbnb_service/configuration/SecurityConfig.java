package com.pbl5cnpm.airbnb_service.configuration;


import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final String[] PUBLIC_END_POINT = {"/users", "/auth/token", "/auth/introspect"};
    
    @Value("${security.secret}")
    private String SIGNER_KEY ;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // httpSecurity.authorizeHttpRequests(request -> request
        //                 .requestMatchers(HttpMethod.POST, PUBLIC_END_POINT).permitAll()
        //                 .anyRequest().authenticated());
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers("/**").permitAll() // Cho phép tất cả các endpoint
        );
        httpSecurity.oauth2ResourceServer(auth2 -> auth2.jwt(JwtConfigurer -> JwtConfigurer.decoder(jwtDecoder())));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec keySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
        // Trả về JwtDecoder sử dụng NimbusJwtDecoder
        return NimbusJwtDecoder.withSecretKey(keySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }
}