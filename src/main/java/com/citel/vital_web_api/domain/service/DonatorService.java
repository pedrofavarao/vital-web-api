package com.citel.vital_web_api.domain.service;

import com.citel.vital_web_api.domain.model.Donator;
import com.citel.vital_web_api.domain.repository.DonatorRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Component
public class DonatorService {
    @Autowired
    private DonatorRepository donatorRepository;

    public Donator saveDonator(Donator donator) {
        if (donator.getAddress() != null) {
            donator.getAddress().setDonator(donator);
        }
        return donatorRepository.save(donator);
    }
}
