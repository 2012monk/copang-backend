package com.alconn.copang.config;

import com.alconn.copang.security.*;
import com.alconn.copang.security.provider.JwtAuthenticationProvider;
import com.alconn.copang.security.provider.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.util.HashSet;
import java.util.Set;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private final CustomUserDetailsService service;

    private final JwtAuthenticationProvider provider;
//
    private final CustomLogoutHandler handler;
//
    private final JwtValidateFilter jwtFilter;

    private final ObjectMapper mapper;

    private final JwtTokenProvider tokenProvider;

//    @Bean
//    public JwtValidateFilter jwtValidateFilter(){
//        return new JwtValidateFilter(service, blackList());
//    }
//    @Bean
//    public CustomUserDetailsService userDetailsService(){
//        return new CustomUserDetailsService(jwtTokenProvider());
//    }

//    @Bean
//    public CustomLogoutHandler customLogoutHandler(){
//        return new CustomLogoutHandler(blackList());
//    }



//    @Bean
//    public JwtAuthenticationProvider jwtAuthenticationProvider(){
//        return new JwtAuthenticationProvider(jwtTokenProvider());
//    }

//    @Bean
//    public JwtTokenProvider jwtTokenProvider(){
//        return new JwtTokenProvider(mapper);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors()
                .and()
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers( "/docs/**").permitAll()
                    .antMatchers("/").permitAll()
                    .antMatchers("/**/login", "/**/signup").permitAll()
//                    .anyRequest().hasRole("GUEST")
                // TODO 권한관계 설정하기
                    .anyRequest().hasAnyRole("GUEST", "CLIENT", "ADMIN")
//                    .anyRequest().authenticated()
                .and()
//                    .csrf().disable()

                    .logout()
                        .addLogoutHandler(handler)
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout", "GET"))
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                    .permitAll()
                //                .antMatchers("/access").hasRole("CLIENT")
                //                .antMatchers("/**/*").permitAll()
                //                .antMatchers(HttpMethod.OPTIONS).permitAll()
                //                .antMatchers(HttpMethod.DELETE).permitAll()
                //                .antMatchers(HttpMethod.HEAD).permitAll()
                //                .antMatchers(HttpMethod.POST).permitAll()
                //                .antMatchers(HttpMethod.PUT).permitAll();
            .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
            .and()
                .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .and()
                   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//                   .addFilterBefore(jwtValidateFilter(), UsernamePasswordAuthenticationFilter.class);

        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(jwtAuthenticationProvider());
        auth.authenticationProvider(provider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/**");
//        web.ignoring().antMatchers("/docs")
//                .antMatchers("/resources/**", "/static/**")
//                .antMatchers("/docs/*", "/static/docs/**")
//                .antMatchers("/docs/**", "/docs/index.html");

//        web.expressionHandler(new DefaultWebSecurityExpressionHandler(){
//            @Override
//            protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
//                WebSecurityExpressionRoot root = (WebSecurityExpressionRoot) super.createSecurityExpressionRoot(authentication, fi);
//                root.setDefaultRolePrefix("");
//                return root;
//            }
//        });
    }


}
