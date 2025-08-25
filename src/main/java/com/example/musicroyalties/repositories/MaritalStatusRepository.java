package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Long> {
}