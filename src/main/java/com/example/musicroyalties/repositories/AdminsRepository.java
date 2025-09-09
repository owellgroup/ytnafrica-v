package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.Admins;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminsRepository extends JpaRepository<Admins, Long> {
}