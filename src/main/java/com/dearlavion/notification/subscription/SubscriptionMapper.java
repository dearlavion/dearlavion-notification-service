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

    void updateCopilot(SubscriptionDTO dto,
                       @MappingTarget CopilotSubscription sub);

    void updateWisher(SubscriptionDTO dto,
                      @MappingTarget WisherSubscription sub);
}
