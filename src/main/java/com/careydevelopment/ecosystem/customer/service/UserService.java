package com.careydevelopment.ecosystem.customer.service;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.careydevelopment.ecosystem.customer.exception.ServiceException;
import com.careydevelopment.ecosystem.customer.model.SalesOwner;
import com.careydevelopment.ecosystem.customer.util.SessionUtil;

import reactor.util.retry.Retry;
import us.careydevelopment.util.api.model.RestResponse;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private WebClient userClient;

    @Autowired
    private SessionUtil sessionUtil;
    
    public UserService(@Value("${ecosystem-user-service.endpoint}") String endpoint) {
        userClient = WebClient.builder().baseUrl(endpoint).filter(WebClientFilter.logRequest())
                .filter(WebClientFilter.logResponse()).filter(WebClientFilter.handleError()).build();
    }

    public SalesOwner fetchUser() {
        final String bearerToken = sessionUtil.getBearerToken();
    
        final RestResponse<SalesOwner> salesOwnerResponse = userClient.get().uri("/me").header(HttpHeaders.AUTHORIZATION, bearerToken)
                .retrieve().bodyToMono(new ParameterizedTypeReference<RestResponse<SalesOwner>>() {})
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)).filter(ex -> WebClientFilter.is5xxException(ex))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new ServiceException(
                                "Max retry attempts reached")))
                .block();

        final SalesOwner salesOwner = salesOwnerResponse.getResponse();
        LOG.debug("User is " + salesOwner);

        return salesOwner;
    }
}
