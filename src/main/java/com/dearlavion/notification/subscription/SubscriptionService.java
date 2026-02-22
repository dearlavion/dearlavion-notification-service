package com.dearlavion.notification.subscription;

import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import com.dearlavion.notification.subscription.dto.SubscriptionDTO;
import com.dearlavion.notification.subscription.dto.SubscriptionType;
import com.dearlavion.notification.subscription.dto.WisherSubscription;
import com.dearlavion.notification.subscription.repository.CopilotSubscriptionRepository;
import com.dearlavion.notification.subscription.repository.WisherSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dearlavion.notification.subscription.dto.SubscriptionType.COPILOT;
import static com.dearlavion.notification.subscription.dto.SubscriptionType.WISHER;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final CopilotSubscriptionRepository copilotRepo;
    private final WisherSubscriptionRepository wisherRepo;
    private final SubscriptionMapper mapper;

    // CREATE
    public Object create(SubscriptionDTO dto) {
        return switch (dto.getType()) {
            case COPILOT -> copilotRepo.save(mapper.toCopilot(dto));
            case WISHER -> wisherRepo.save(mapper.toWisher(dto));
        };
    }

    // UPDATE
    public Object update(String id, SubscriptionDTO dto) {
        return switch (dto.getType()) {
            case COPILOT -> {
                CopilotSubscription sub = copilotRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Copilot subscription not found"));
                mapper.updateCopilot(dto, sub);
                yield copilotRepo.save(sub);
            }
            case WISHER -> {
                WisherSubscription sub = wisherRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Wisher subscription not found"));
                mapper.updateWisher(dto, sub);
                yield wisherRepo.save(sub);
            }
        };
    }

    // DELETE
    public void delete(String id, SubscriptionType type) {
        switch (type) {
            case COPILOT -> copilotRepo.deleteById(id);
            case WISHER -> wisherRepo.deleteById(id);
        }
    }

    // FIND BY ID
    public Object findById(String id, SubscriptionType type) {
        return switch (type) {
            case COPILOT -> copilotRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Copilot subscription not found"));
            case WISHER -> wisherRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Wisher subscription not found"));
        };
    }

    // FIND BY USERID
    public List<?> findByUsername(String username, SubscriptionType type) {
        return switch (type) {
            case COPILOT -> copilotRepo.findByUsername(username);
            case WISHER -> wisherRepo.findByUsername(username);
        };
    }

    public Object patch(String id, SubscriptionType type, SubscriptionDTO dto) {
        return switch (type) {
            case COPILOT -> {
                CopilotSubscription sub = copilotRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Copilot subscription not found"));

                applyPatchToCopilot(dto, sub);
                yield copilotRepo.save(sub);
            }
            case WISHER -> {
                WisherSubscription sub = wisherRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Wisher subscription not found"));

                applyPatchToWisher(dto, sub);
                yield wisherRepo.save(sub);
            }
        };
    }

    private void applyPatchToCopilot(SubscriptionDTO dto, CopilotSubscription sub) {
        if (dto.getIsActive() != null)
            sub.setIsActive(dto.getIsActive());
    }

    private void applyPatchToWisher(SubscriptionDTO dto, WisherSubscription sub) {
        if (dto.getIsActive() != null)
            sub.setIsActive(dto.getIsActive());
    }
}
