package com.debosz.kamil.service.driver;

import com.debosz.kamil.domain.driver.Driver;
import com.debosz.kamil.domain.driver.PersonalInformation;
import com.debosz.kamil.repository.DriverRepository;
import com.debosz.kamil.util.CodeGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class DriverService implements IDriverService {

    private DriverRepository driverRepository;

    private static final Integer CARD_NUMBER_LENGHT = 6;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Optional<Driver> findById(Long id) {
        return driverRepository.findById(id);
    }

    @Override
    public Driver save(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public Optional<Driver> getByCardCode(String cardCode) {
        return driverRepository.getByCardCode(cardCode);
    }

    @Override
    public Driver issueSubscriptionCard(PersonalInformation personalInformation) {
        Driver driver = Driver.builder()
                                .personalInformation(personalInformation)
                                .cardCode(CodeGenerator.generateCode(CARD_NUMBER_LENGHT))
                                .expirationTime(LocalDate.now().plus(1, ChronoUnit.MONTHS))
                                .build();
        driver = driverRepository.save(driver);
        return driver;
    }
}
