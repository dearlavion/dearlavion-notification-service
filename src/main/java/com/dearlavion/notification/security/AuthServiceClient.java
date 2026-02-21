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

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth.server.url}")
    private String authServiceUrl;

    public AuthVerificationResponse verify(String token) {
        String url = authServiceUrl + "/auth/verify";

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

    public UserDto getUserDetails(String username) {
        String url = authServiceUrl + "/auth/user/" + username;

        try {
            return restTemplate.getForObject(
                    url,
                    UserDto.class
            );
        } catch (Exception e) {
            log.error("Failed to fetch user details at URL: {}", url, e);
            return null;
        }
    }
}
