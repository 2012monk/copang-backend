package com.alconn.copang.config;

import com.alconn.copang.security.CustomAccessDeniedHandler;
import com.alconn.copang.security.CustomLogoutHandler;
import com.alconn.copang.security.JwtValidateFilter;
import com.alconn.copang.security.RestAuthenticationEntryPoint;
import com.alconn.copang.security.privider.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import java.util.Set;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //    @Autowired
//    private final CustomUserDetailsService service;

    private final JwtAuthenticationProvider provider;

    private final Set<String> blackList;

    private final CustomLogoutHandler handler;

    private final JwtValidateFilter jwtFilter;


//    @Bean
//    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
//        return new GrantedAuthorityDefaults("");
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
                    .csrf().disable()
                    .logout()
                        .logoutUrl("/api/auth/logout")
                        .addLogoutHandler(handler)
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

        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/**");
        web.ignoring().antMatchers("/docs")
                .antMatchers("/resources/**", "/static/**")
                .antMatchers("/docs/*", "/static/docs/**")
                .antMatchers("/docs/**", "/docs/index.html");

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
