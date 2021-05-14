package com.careydevelopment.ecosystem.customer.service;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.careydevelopment.ecosystem.customer.model.SalesOwner;

import reactor.util.retry.Retry;


@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    
    private WebClient userClient; 
    
    
    public UserService(@Value("${ecosystem-user-service.endpoint}") String endpoint) {
        userClient = WebClient
	        		.builder()
	        		.baseUrl(endpoint)
	        		.filter(WebClientFilter.logRequest())
	        		.filter(WebClientFilter.logResponse())
	        		.filter(WebClientFilter.handleError())
	        		.build();
    }
    
    
    public SalesOwner fetchUser(String bearerToken) {
        SalesOwner salesOwner = userClient.get()
                .uri("/user/me")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .retrieve()
                .bodyToMono(SalesOwner.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                		.filter(ex -> WebClientFilter.is5xxException(ex))
                		.onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> 
                			new ServiceException("Max retry attempts reached", HttpStatus.SERVICE_UNAVAILABLE.value())))
                .block();
        
        LOG.debug("User is " + salesOwner);
        
        return salesOwner;
    }
}
