package com.dearlavion.notification.inApp.websocket;

import com.dearlavion.notification.security.AuthServiceClient;
import com.dearlavion.notification.security.AuthVerificationResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.*;
import org.springframework.web.socket.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final AuthServiceClient authService;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws Exception {

        if (request instanceof ServletServerHttpRequest servletRequest) {

            // ✅ GET TOKEN FROM QUERY PARAM (NOT HEADER)
            String token = servletRequest.getServletRequest().getParameter("token");

            System.out.println("Handshake token: " + token);

            if (token != null) {
                AuthVerificationResponse user = authService.verify(token);

                if (user != null && user.isValid()) {
                    attributes.put("username", user.getUsername());
                    System.out.println("User verified: " + user.getUsername());
                }
            } else {
                System.out.println("⚠️ No token in handshake");
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {}
}
