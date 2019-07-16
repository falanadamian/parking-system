package com.debosz.kamil.service.parking;

import com.debosz.kamil.domain.parking.Parking;

public interface IParkingService {

    Parking getParking();

    Boolean isSlotAvailable();

    Boolean isSubscriptionAvailable();

    void incrementTakenRegularSpotsNumber();

    void decrementTakenRegularSpotsNumber();

    void incrementTakenSubscriptionSpotsNumber();

    void decrementTakenSubscriptionSpotsNumber();

    void incrementSubscriptionsNumber();
}
