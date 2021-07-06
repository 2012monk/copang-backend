package com.alconn.copang.config;

import java.util.HashSet;
import java.util.Set;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    //
    @Bean
    public Set<String> blackList() {
        return new HashSet<>();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory f =
            new HttpComponentsClientHttpRequestFactory();
        f.setReadTimeout(5000);
        f.setConnectTimeout(3000);
        HttpClient client = HttpClientBuilder.create()
            .setMaxConnTotal(100)
            .setMaxConnPerRoute(5)
            .build();
        f.setHttpClient(client);
        return new RestTemplate(f);
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
