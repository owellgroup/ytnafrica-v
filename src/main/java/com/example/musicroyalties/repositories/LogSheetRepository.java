package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.ArtistWork;
import com.example.musicroyalties.models.LogSheet;
import com.example.musicroyalties.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogSheetRepository extends JpaRepository<LogSheet, Long> {
    List<LogSheet> findByCompany(Company company);
    List<LogSheet> findByUserId(Long userId);
}
