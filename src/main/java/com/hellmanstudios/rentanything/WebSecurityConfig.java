package com.hellmanstudios.rentanything;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig {

    private static final AntPathRequestMatcher[] WHITE_LIST_URLS = {
            antMatcher("/"),
            antMatcher("/login"),
            antMatcher("/register"),
            antMatcher("/items"),
            antMatcher("/items/**"),
            antMatcher("/categories"),
            antMatcher("/categories/**"),
            antMatcher("/search")
    };

    private final UserDetailServiceImpl userDetailService;

    public WebSecurityConfig(UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }

    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
        authorize -> authorize
        .requestMatchers(antMatcher("/css/**")).permitAll()
        .requestMatchers(antMatcher("/js/**")).permitAll()
        .requestMatchers(antMatcher("/uploads/**")).permitAll()
        .requestMatchers(antMatcher("/files/**")).permitAll()
        .requestMatchers(antMatcher("/storage/**")).permitAll()
        .requestMatchers(antMatcher("/api/users/**")).hasAuthority("ROLE_ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/**").hasAuthority("ROLE_ADMIN")
        .requestMatchers(HttpMethod.PUT, "/api/**").hasAuthority("ROLE_ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/**").hasAuthority("ROLE_ADMIN")
        .requestMatchers(antMatcher("/admin/**")).hasAuthority("ROLE_ADMIN")
        .requestMatchers(WHITE_LIST_URLS).permitAll()
        .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults())
        .headers(headers -> 
        headers.frameOptions(frameOptions -> frameOptions 
                .disable())) // for h2console
        .formLogin(formlogin -> 
            formlogin
                .loginPage("/login")
                .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
                .permitAll())
        .logout(logout -> logout.permitAll())
        .csrf(csrf -> csrf.disable()); // not for production, just for development
        return http.build();
    }

    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(new BCryptPasswordEncoder());
	}

}
