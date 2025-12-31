package com.ms.music_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        //Public
                        .requestMatchers(HttpMethod.GET,
                                "/musics/**",
                                "/musics/*/comments",
                                "/musics/*/annotations",
                                "/musics/search",
                                "/artists/**",
                                "/actuator",
                                "/actuator/health",
                                "/actuator/prometheus").permitAll()

                        //Authenticated
                        .requestMatchers(HttpMethod.POST,
                                "/musics/*/like", "/musics/*/comments", "/musics/*/annotations").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/musics/*/like").authenticated()

                        //Admin
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        //Fallback
                        .anyRequest().denyAll()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new InMemoryUserDetailsManager();
    }
}