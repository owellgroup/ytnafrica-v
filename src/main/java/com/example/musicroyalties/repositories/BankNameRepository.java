package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.BankName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankNameRepository extends JpaRepository<BankName, Long> {
}