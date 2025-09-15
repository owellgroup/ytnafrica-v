package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.license.VatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VatStatusRepository extends JpaRepository<VatStatus, Long> {
}