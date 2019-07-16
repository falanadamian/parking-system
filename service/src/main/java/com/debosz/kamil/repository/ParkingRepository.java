package com.debosz.kamil.repository;

import com.debosz.kamil.domain.parking.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, String> {

    Optional<Parking> findById(String id);
}
