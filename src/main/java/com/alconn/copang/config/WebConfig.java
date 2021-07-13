package com.alconn.copang.config;

import com.alconn.copang.search.QueryStringResolver;
import com.alconn.copang.security.aop.UserIdResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@RequiredArgsConstructor
@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserIdResolver userIdResolver;

    private final QueryStringResolver queryStringResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns(
                "http://127.0.0.1:5500",
                "http://localhost:3000",
                "http://localhost:47788",
                "http://localhost:8080",
                "https://alconn.co",
                "https://deli.alconn.co",
                "https://copang.alconn.co",
                "https://seller.alconn.co"
            )
            .allowedHeaders("*")
            .allowedMethods("*")
            .allowCredentials(true)
            .maxAge(3600L);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
            .setCachePeriod(3600)
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(new PathResourceResolver())
        ;

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdResolver);
        resolvers.add(queryStringResolver);
    }

    @Bean
    public ViewResolver viewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setViewClass(InternalResourceView.class);
        return resolver;
    }


}
