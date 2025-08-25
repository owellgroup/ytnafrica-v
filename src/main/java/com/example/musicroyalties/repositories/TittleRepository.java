package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.Tittle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TittleRepository extends JpaRepository<Tittle, Long> {
}