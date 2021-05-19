package com.careydevelopment.ecosystem.customer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Value("${ip.whitelist}")
    private String[] ipWhitelist;


    
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new RateLimitInterceptor())
//            .addPathPatterns("/**");
//    }
    
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
          registry.addInterceptor(new IpCheckerInterceptor(ipWhitelist))
              .addPathPatterns("/**");
      }
}
