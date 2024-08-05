package com.citel.vital_web_api.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodTypeDonorCountDTO {
    private String bloodType;
    private int donorCount;
}
