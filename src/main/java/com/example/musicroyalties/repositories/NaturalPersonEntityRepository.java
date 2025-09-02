package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.license.NaturalPersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaturalPersonEntityRepository extends JpaRepository<NaturalPersonEntity, Long> {
}