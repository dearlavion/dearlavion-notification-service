package org.example.email;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test-email")
public class EmailTestController {

    private final EmailService emailService;

    @GetMapping
    public String sendTestEmail() {

        emailService.sendWelcomeEmail(
                "dearlavion@gmail.com",
                "Test User"
        );

        return "Email sent!";
    }
}

