package com.debosz.kamil.domain.parking;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Parking {

    private static Parking parking;
    private static final Integer PARKING_SPOTS_NUMBER = 100;

    @Id
    @Column(name = "id")
    private String id = Parking.class.getSimpleName();

    @Column(name = "parking_spots_number")
    private Integer spotsNumber = PARKING_SPOTS_NUMBER;

    @Column(name = "parking_taken_spots_number")
    private Integer takenRegularSpotsNumber = 0;

    @Column(name = "parking_subscriptions_number")
    private Integer subscriptionsNumber = 0;

    @Column(name = "parking_taken_subscription_spots_number")
    private Integer takenSubscriptionSpotsNumber = 0;

    public static Parking getParking() {
        if (Objects.isNull(parking)) {
            parking = new Parking();
        }
        return parking;
    }

    public static Parking setParking(Parking p) {
        if (Objects.isNull(parking)) {
            parking = p;
        }
        return parking;
    }

    public void incrementTakenRegularSpotsNumber() {
        takenRegularSpotsNumber += 1;
    }

    public void decrementTakenRegularSpotsNumber() {
        takenRegularSpotsNumber -= 1;
    }

    public void incrementTakenSubscriptionSpotsNumber() {
        takenSubscriptionSpotsNumber += 1;
    }

    public void decrementTakenSubscriptionSpotsNumber() {
        takenSubscriptionSpotsNumber -= 1;
    }

    public void incrementSubscriptionsNumber() {
        subscriptionsNumber += 1;
    }
}
