package com.pbl5cnpm.airbnb_service.configuration;

import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.apache.catalina.filters.CorsFilter;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final String[] PUBLIC_END_POINT = {"/api/users", "/auth/token", "/auth/introspect"};
    private final String[] PUBLIC_END_POINT_TEST = {"/api/categories"};
    
    @Value("${security.secret}")
    private String SIGNER_KEY ;
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Thêm cấu hình CORS đúng cách
            .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF nếu không cần
            .authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.GET, "/hello", "/api/users").permitAll()
                .requestMatchers(HttpMethod.POST, PUBLIC_END_POINT_TEST).permitAll()
                .requestMatchers(HttpMethod.POST, PUBLIC_END_POINT).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(auth2 -> auth2.jwt(JwtConfigurer -> JwtConfigurer.decoder(jwtDecoder())));

        return httpSecurity.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
// @Bean
    // SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        
    //     httpSecurity.authorizeHttpRequests(request -> request
    //             .requestMatchers(HttpMethod.GET, "/hello","/api/users").permitAll()
    //             .requestMatchers(HttpMethod.POST, PUBLIC_END_POINT_TEST).permitAll()
    //             .requestMatchers(HttpMethod.POST, PUBLIC_END_POINT).permitAll()
    //             .anyRequest().authenticated());
        
    //     httpSecurity.oauth2ResourceServer(auth2 -> auth2.jwt(JwtConfigurer -> JwtConfigurer.decoder(jwtDecoder())));
    //     httpSecurity.csrf(AbstractHttpConfigurer::disable);
        
    //     return httpSecurity.build();
    // }