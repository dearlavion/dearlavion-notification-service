package com.dearlavion.notification.subscription;

import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import com.dearlavion.notification.subscription.dto.SubscriptionDTO;
import com.dearlavion.notification.subscription.dto.WisherSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    CopilotSubscription toCopilot(SubscriptionDTO dto);
    WisherSubscription toWisher(SubscriptionDTO dto);
    void updateCopilot(@MappingTarget CopilotSubscription sub, SubscriptionDTO dto);
    void updateWisher(@MappingTarget WisherSubscription sub, SubscriptionDTO dto);
}
