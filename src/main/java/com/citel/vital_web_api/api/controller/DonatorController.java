package com.citel.vital_web_api.api.controller;

import com.citel.vital_web_api.domain.model.dto.AverageAgeByBloodTypeDto;
import com.citel.vital_web_api.domain.model.Donator;
import com.citel.vital_web_api.domain.model.dto.BloodTypeDonorCountDTO;
import com.citel.vital_web_api.domain.model.dto.IMCAverageResponseDto;
import com.citel.vital_web_api.domain.model.dto.ObesityPercentageDto;
import com.citel.vital_web_api.domain.repository.DonatorRepository;
import com.citel.vital_web_api.domain.service.DonatorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return donatorService.getAllDonators();
    }

    @GetMapping("/{donatorId}")
    public Donator getDonator(@PathVariable Long donatorId){
        return donatorService.getDonator(donatorId);
    }

    @GetMapping("/imc-average")
    public List<IMCAverageResponseDto> getIMCAverageByAgeRange() {
        Map<String, Double> imcAverages = donatorService.calculateAverageIMCByAgeRange();
        return imcAverages.entrySet().stream()
                .map(entry -> new IMCAverageResponseDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping("/average-age-by-blood-type")
    public ResponseEntity<List<AverageAgeByBloodTypeDto>> getAverageAgeByBloodType() {
        List<AverageAgeByBloodTypeDto> averageAgeByBloodType = donatorService.calculateAverageAgeByBloodType();
        return ResponseEntity.ok(averageAgeByBloodType);
    }

    @GetMapping("/obesity-percentage")
    public List<ObesityPercentageDto> getObesityPercentageByGender() {
        return donatorService.calculateObesityPercentageByGender();
    }

    @GetMapping("/possible-donors")
    public List<BloodTypeDonorCountDTO> getPossibleDonors() {
        return donatorService.calculatePossibleDonors();
    }
}
