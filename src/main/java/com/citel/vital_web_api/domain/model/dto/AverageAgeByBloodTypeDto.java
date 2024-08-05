package com.citel.vital_web_api.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AverageAgeByBloodTypeDto {
    private String bloodType;
    private Double averageAge;
}
