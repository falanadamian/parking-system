package com.debosz.kamil.service.driver;

import com.debosz.kamil.domain.driver.Driver;
import com.debosz.kamil.domain.driver.PersonalInformation;

import java.util.Optional;

public interface IDriverService {

    Optional<Driver> findById(Long id);

    Driver save(Driver driver);

    Optional<Driver> getByCardCode(String cardCode);

    Driver issueSubscriptionCard(PersonalInformation personalInformation);
}
