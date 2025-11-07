package com.c4.routy.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                   // origin 이후 요청 경로 패턴
                .allowedOrigins("*")                            // origin 등록(어떤 front 서버든 상관 없이)
                .allowedOrigins("http://localhost:5173")        // origin 등록
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
