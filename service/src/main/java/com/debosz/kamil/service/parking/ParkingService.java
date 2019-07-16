package com.debosz.kamil.service.parking;

import com.debosz.kamil.domain.parking.Parking;
import com.debosz.kamil.repository.ParkingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParkingService implements IParkingService {

    private ParkingRepository parkingRepository;

    public ParkingService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
        fetchParking();
    }

    @Override
    public Parking getParking() {
        return parkingRepository.getOne(Parking.class.getSimpleName());
    }

    @Override
    public Boolean isSlotAvailable() {
        Parking parking = getParking();
        return parking.getSpotsNumber() - parking.getTakenRegularSpotsNumber() - (parking.getSubscriptionsNumber() - parking.getTakenSubscriptionSpotsNumber()) > 0;
    }

    @Override
    public Boolean isSubscriptionAvailable() {
        Parking parking = getParking();
        return parking.getSpotsNumber() - parking.getSubscriptionsNumber() != 0;
    }

    @Override
    public void incrementTakenRegularSpotsNumber() {
        Parking parking = getParking();
        parking.incrementTakenRegularSpotsNumber();
        parkingRepository.save(parking);
    }

    @Override
    public void decrementTakenRegularSpotsNumber(){
        Parking parking = getParking();
        parking.decrementTakenRegularSpotsNumber();
        parkingRepository.save(parking);
    }

    @Override
    public void incrementTakenSubscriptionSpotsNumber(){
        Parking parking = getParking();
        parking.incrementSubscriptionsNumber();
        parkingRepository.save(parking);
    }

    @Override
    public void decrementTakenSubscriptionSpotsNumber(){
        Parking parking = getParking();
        parking.decrementTakenSubscriptionSpotsNumber();
        parkingRepository.save(parking);
    }

    @Override
    public void incrementSubscriptionsNumber(){
        Parking parking = getParking();
        parking.incrementSubscriptionsNumber();
        parkingRepository.save(parking);
    }

    private void fetchParking() {
        Optional<Parking> optionalParking = parkingRepository.findById(Parking.class.getSimpleName());
        if (optionalParking.isPresent()) {
            Parking.setParking(optionalParking.get());
        } else {
            Parking parking = Parking.getParking();
            parkingRepository.save(parking);
        }
    }
}
