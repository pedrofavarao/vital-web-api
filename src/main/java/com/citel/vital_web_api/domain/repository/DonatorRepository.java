package com.citel.vital_web_api.domain.repository;

import com.citel.vital_web_api.domain.model.Donator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonatorRepository extends JpaRepository<Donator, Long> {
}
