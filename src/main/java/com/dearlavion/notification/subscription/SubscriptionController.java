package com.dearlavion.notification.subscription;

import com.dearlavion.notification.subscription.dto.SubscriptionDTO;
import com.dearlavion.notification.subscription.dto.SubscriptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification/subscription")
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

    @GetMapping("/user/{username}")
    public List<?> findByUsername(@PathVariable String username, @RequestParam SubscriptionType type) {
        return service.findByUsername(username, type);
    }

    @PatchMapping("/{id}")
    public Object patch(
            @PathVariable String id,
            @RequestParam SubscriptionType type,
            @RequestBody SubscriptionDTO dto
    ) {
        return service.patch(id, type, dto);
    }
}



