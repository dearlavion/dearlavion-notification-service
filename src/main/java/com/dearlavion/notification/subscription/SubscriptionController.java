package com.dearlavion.notification.subscription;

import com.dearlavion.notification.subscription.dto.SubscriptionDTO;
import com.dearlavion.notification.subscription.dto.SubscriptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @PostMapping
    public Object create(@RequestBody SubscriptionDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable String id, @RequestBody SubscriptionDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id, @RequestParam SubscriptionType type) {
        service.delete(id, type);
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable String id, @RequestParam SubscriptionType type) {
        return service.findById(id, type);
    }

    @GetMapping("/user/{userId}")
    public List<?> findByUserId(@PathVariable String userId, @RequestParam SubscriptionType type) {
        return service.findByUserId(userId, type);
    }
}



