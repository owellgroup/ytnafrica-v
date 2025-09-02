package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.license.SourceOfMusic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceOfMusicRepository extends JpaRepository<SourceOfMusic, Long> {
}