package com.dearlavion.notification.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthServiceClient {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceClient.class);

    // Keep direct instantiation of RestTemplate
    private final RestTemplate restTemplate = new RestTemplate();

    // Inject URL from application.yml or environment
    @Value("${auth.server.url}")
    private String authServiceUrl;

    public AuthVerificationResponse verify(String token) {
        String url = authServiceUrl + "/auth/verify"; // append endpoint
        log.info("Calling auth service at URL: {}", url);

        try {
            return restTemplate.postForObject(
                    url,
                    Map.of("token", token),
                    AuthVerificationResponse.class
            );
        } catch (Exception e) {
            log.error("Failed to call auth service at URL: {}", url, e);
            return null; // token invalid or service unavailable
        }
    }
}
