package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.license.LegalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LegalEntityRepository extends JpaRepository<LegalEntity, Long> {
}