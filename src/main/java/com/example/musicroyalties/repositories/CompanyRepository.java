package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.Company;
import com.example.musicroyalties.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByUser(User user);
}
