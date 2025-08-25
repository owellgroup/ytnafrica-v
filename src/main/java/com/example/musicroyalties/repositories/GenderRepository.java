package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<Gender, Long> {
}