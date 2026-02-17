package com.dearlavion.notification.subscription;

import com.dearlavion.notification.kafka.dto.WishEvent;
import com.dearlavion.notification.subscription.dto.CopilotSubscription;
import com.dearlavion.notification.subscription.repository.CopilotSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionMatcherService {

    private final CopilotSubscriptionRepository copilotRepo;

    public List<CopilotSubscription> match(WishEvent wish) {
        // Step 1: Get all active subscriptions in same country & city
        List<CopilotSubscription> subs = copilotRepo.findByCountryAndCityAndIsActive(
                wish.getCountryCode(), wish.getCityName(), true
        );

        // Step 2: Filter by radius (if RADIUS coverage type)
        subs = subs.stream()
                .filter(sub -> {
                    if ("RADIUS".equalsIgnoreCase(sub.getCoverageType()) && sub.getCenterLocation() != null) {
                        List<Double> coords = sub.getCenterLocation().getCoordinates();
                        double lat = coords.get(1);
                        double lng = coords.get(0);

                        double distanceKm = distanceInKm(lat, lng, wish.getLatitude(), wish.getLongitude());
                        return distanceKm <= sub.getRadiusKm();
                    }
                    return true; // no radius limit
                })
                .collect(Collectors.toList());

        // Step 3: Filter by categories
        subs = subs.stream()
                .filter(sub -> sub.getCategories() == null || sub.getCategories().isEmpty() ||
                        wish.getCategories().stream().anyMatch(sub.getCategories()::contains)
                )
                .collect(Collectors.toList());

        // Step 4: Filter by minimum amount
        subs = subs.stream()
                .filter(sub -> {
                    // ---- Min Amount Filter ----
                    if (sub.getMinAmount() != null && wish.getAmount() != null && wish.getAmount().compareTo(sub.getMinAmount()) < 0) {
                        return false;
                    }
                    // ---- Paid Filter ----
                    if (sub.getIsPaid() != null && wish.getIsPaid() != null && !sub.getIsPaid().equals(wish.getIsPaid())) {
                        return false;
                    }
                    return true;
                })
                .toList();
        return subs;
    }

    // Haversine formula to calculate distance between two lat/lng points
    private double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }
}

