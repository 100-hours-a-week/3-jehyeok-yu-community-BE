package com.kakaotechbootcamp.community.utils.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtTokenFilter,
//        FilterErrorResponseWriter writer)
//        throws Exception {
//        http
//            .csrf(AbstractHttpConfigurer::disable)
//            .formLogin(AbstractHttpConfigurer::disable)
//            .httpBasic(AbstractHttpConfigurer::disable)
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
//                .requestMatchers(HttpMethod.POST, "/users").permitAll()
//                .requestMatchers("/terms").permitAll()
//                .requestMatchers("/privacy").permitAll()
//                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .anyRequest().authenticated()
//            )
//            .exceptionHandling(eh -> eh
//                .authenticationEntryPoint(
//                    (req, res, ex) -> writer.write(res, CommonErrorCode.AUTH_UNAUTHORIZED))
//                .accessDeniedHandler(
//                    (req, res, ex) -> writer.write(res, CommonErrorCode.AUTH_FORBIDDEN))
//            )
//            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

    // 비밀번호 암호화 객체 생성
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://localhost:3000"));
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(true);
//        config.setExposedHeaders(List.of("Authorization"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
}
