package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {
}