package com.alconn.copang.config;

import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;

import java.util.HashSet;
import java.util.Set;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeanConfig {

//
    @Bean
    public Set<String> blackList(){
        return new HashSet<>();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public GrantedAuthoritiesMapper authoritiesMapper() {
//        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
////        mapper.setPrefix(""); // this line is not required
//        mapper.setConvertToUpperCase(true); // convert your roles to uppercase
//        mapper.setDefaultAuthority("GUEST"); // set a default role
//
//        return mapper;
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        // your config ...
//        provider.setAuthoritiesMapper(authoritiesMapper());
//
//        return provider;
//    }
}
