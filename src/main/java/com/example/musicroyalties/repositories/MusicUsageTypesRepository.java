package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.license.MusicUsageTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicUsageTypesRepository extends JpaRepository<MusicUsageTypes, Integer> {
}