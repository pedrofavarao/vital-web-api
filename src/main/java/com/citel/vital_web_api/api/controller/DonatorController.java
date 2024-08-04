package com.citel.vital_web_api.api.controller;

import com.citel.vital_web_api.domain.model.Donator;
import com.citel.vital_web_api.domain.repository.AddressRepository;
import com.citel.vital_web_api.domain.repository.DonatorRepository;
import com.citel.vital_web_api.domain.service.AddressService;
import com.citel.vital_web_api.domain.service.DonatorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donator")
@AllArgsConstructor
public class DonatorController {

    @Autowired
    private DonatorService donatorService;
    @Autowired
    private DonatorRepository donatorRepository;

    @PostMapping
    public Donator createDonator(@Valid @RequestBody Donator donator) {
        return donatorService.saveDonator(donator);
    }

    @GetMapping
    public List<Donator> getDonators(){
        return donatorRepository.findAll();
    }

    @GetMapping("/{donatorId}")
    public ResponseEntity<Donator> getDonator(@PathVariable Long donatorId){
        return donatorRepository.findById(donatorId).map(donator -> ResponseEntity.ok(donator))
                .orElse(ResponseEntity.notFound().build());
    }
}
