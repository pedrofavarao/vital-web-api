package com.citel.vital_web_api.domain.service;

import com.citel.vital_web_api.domain.model.dto.AverageAgeByBloodTypeDto;
import com.citel.vital_web_api.domain.model.Donator;
import com.citel.vital_web_api.domain.model.dto.BloodTypeDonorCountDTO;
import com.citel.vital_web_api.domain.model.dto.ObesityPercentageDto;
import com.citel.vital_web_api.domain.repository.DonatorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Component
public class DonatorService {
    @Autowired
    private DonatorRepository donatorRepository;

    public Donator saveDonator(Donator donator) {
        if (donator.getAddress() != null) {
            donator.getAddress().setDonator(donator);
            donator.setImc();
        }
        return donatorRepository.save(donator);
    }

    public List<Donator> getAllDonators() {
        List<Donator> donators = donatorRepository.findAll();
        donators.forEach(donator -> {
            donator.setBloodDonation(getCanDonateTo(donator.getBloodType()));
            donator.setBloodReceive(getCanReceiveTo(donator.getBloodType()));
        });
        return donators;
    }

    public Donator getDonator(Long donatorId){
        Optional<Donator> optionalDonator = donatorRepository.findById(donatorId);
        if (optionalDonator.isPresent()) {
            Donator donator = optionalDonator.get();
            donator.setBloodDonation(this.getCanDonateTo(donator.getBloodType()));
            donator.setBloodReceive(this.getCanReceiveTo(donator.getBloodType()));
            return donator;
        } else {
            throw new EntityNotFoundException("Donator not found with id: " + donatorId);
        }
    }

    // Função para calcular o IMC médio por faixa etária
    public Map<String, Double> calculateAverageIMCByAgeRange() {
        List<Donator> donators = donatorRepository.findAll(); // Obtém todos os doadores

        // Agrupa os doadores por faixa etária
        Map<String, List<Donator>> groupedByAgeRange = donators.stream()
                .collect(Collectors.groupingBy(this::getAgeRange));

        // Calcula o IMC médio para cada faixa etária
        return groupedByAgeRange.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateAverageIMC(entry.getValue())
                ));
    }

    // Calcula o IMC médio para uma lista de doadores
    private double calculateAverageIMC(List<Donator> donators) {
        double averageAge = donators.stream()
                .mapToInt(Donator::getAge) // Mapeia idades para int
                .average() // Calcula a média
                .orElse(0.0); // Retorna 0.0 se não houver doadores

        // Arredonda para duas casas decimais
        BigDecimal roundedAverageAge = new BigDecimal(averageAge).setScale(2, RoundingMode.HALF_UP);
        return roundedAverageAge.doubleValue();
    }

    // Determina a faixa etária de um doador
    private String getAgeRange(Donator donator) {
        int age = donator.getAge();
        int lowerBound = (age / 10) * 10 + 1;
        int upperBound = lowerBound + 9;
        return String.format("%d - %d", lowerBound, upperBound);
    }

    // Função para calcular a média de idade por tipo sanguíneo
    public List<AverageAgeByBloodTypeDto> calculateAverageAgeByBloodType() {
        List<Donator> donators = donatorRepository.findAll();

        Map<String, List<Donator>> groupedByBloodType = donators.stream()
                .collect(Collectors.groupingBy(Donator::getBloodType));

        return groupedByBloodType.entrySet().stream()
                .map(entry -> new AverageAgeByBloodTypeDto(
                        entry.getKey(), // Tipo sanguíneo
                        calculateAverageAge(entry.getValue()) // Média de idade
                ))
                .collect(Collectors.toList());
    }

    // Função auxiliar para calcular a média de idade
    private double calculateAverageAge(List<Donator> donators) {
        double averageAge = donators.stream()
                .mapToInt(Donator::getAge) // Mapeia idades para int
                .average() // Calcula a média
                .orElse(0.0); // Retorna 0.0 se não houver doadores

        // Arredonda para duas casas decimais
        BigDecimal roundedAverageAge = new BigDecimal(averageAge).setScale(2, RoundingMode.HALF_UP);
        return roundedAverageAge.doubleValue();
    }

    // Função para calcular o percentual de obesos por gênero
    public List<ObesityPercentageDto> calculateObesityPercentageByGender() {
        List<Donator> donators = donatorRepository.findAll(); // Obtém todos os doadores

        // Conta o total de doadores por gênero
        Map<String, Long> totalByGender = donators.stream()
                .collect(Collectors.groupingBy(Donator::getGender, Collectors.counting()));

        // Conta o total de obesos por gênero
        Map<String, Long> obeseByGender = donators.stream()
                .filter(donator -> donator.getImc() > 30)
                .collect(Collectors.groupingBy(Donator::getGender, Collectors.counting()));

        // Calcula o percentual de obesos por gênero e converte para DTO
        return totalByGender.entrySet().stream()
                .map(entry -> {
                    String gender = entry.getKey();
                    long total = entry.getValue();
                    long obeseCount = obeseByGender.getOrDefault(gender, 0L);
                    double percentage = total == 0 ? 0.0 : (double) obeseCount / total * 100;
                    return new ObesityPercentageDto(gender, percentage);
                })
                .collect(Collectors.toList());
    }

    public List<String> getCanDonateTo(String bloodType) {
        Map<String, List<String>> donationMap = new HashMap<>();
        donationMap.put("O-", Arrays.asList("O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"));
        donationMap.put("O+", Arrays.asList("O+", "A+", "B+", "AB+"));
        donationMap.put("A-", Arrays.asList("A-", "A+", "AB-", "AB+"));
        donationMap.put("A+", Arrays.asList("A+", "AB+"));
        donationMap.put("B-", Arrays.asList("B-", "B+", "AB-", "AB+"));
        donationMap.put("B+", Arrays.asList("B+", "AB+"));
        donationMap.put("AB-", Arrays.asList("AB-", "AB+"));
        donationMap.put("AB+", Arrays.asList("AB+"));

        return donationMap.getOrDefault(bloodType, Arrays.asList());
    }

    public List<String> getCanReceiveTo(String bloodType) {
        Map<String, List<String>> donationMap = new HashMap<>();
        donationMap.put("O-", Arrays.asList("O-"));
        donationMap.put("O+", Arrays.asList("O+","O-"));
        donationMap.put("A-", Arrays.asList("A-", "O-"));
        donationMap.put("A+", Arrays.asList("A+", "A-", "O+", "O-"));
        donationMap.put("B-", Arrays.asList("B-", "O-"));
        donationMap.put("B+", Arrays.asList("B+", "B-", "O+", "O-"));
        donationMap.put("AB-", Arrays.asList("A-", "B-", "O-", "AB-"));
        donationMap.put("AB+", Arrays.asList("A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-"));

        return donationMap.getOrDefault(bloodType, Arrays.asList());
    }

    public List<BloodTypeDonorCountDTO> calculatePossibleDonors() {
        List<Donator> donators = getAllDonators();

        // Map para contar doadores para cada tipo sanguíneo
        Map<String, Integer> donorCount = new HashMap<>();

        // Percorre todos os doadores
        for (Donator donator : donators) {
            List<String> canDonateToList = getCanDonateTo(donator.getBloodType());
            for (String bloodType : canDonateToList) {
                donorCount.put(bloodType, donorCount.getOrDefault(bloodType, 0) + 1);
            }
        }

        // Converte o Map para uma lista de DTOs
        return donorCount.entrySet().stream()
                .map(entry -> new BloodTypeDonorCountDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}