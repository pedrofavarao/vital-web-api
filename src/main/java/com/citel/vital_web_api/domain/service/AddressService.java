package com.citel.vital_web_api.domain.service;

import com.citel.vital_web_api.domain.model.Address;
import com.citel.vital_web_api.domain.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Component
public class AddressService {
    private AddressRepository repository;

    public Address save(Address address){
        return repository.save(address);
    }
}
