package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.ArtistWork;
import com.example.musicroyalties.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistWorkRepository extends JpaRepository<ArtistWork, Long> {
    List<ArtistWork> findByUser(User user);
    List<ArtistWork> findByUserId(Long userId);
    List<ArtistWork> findByStatusId(Long statusId);
}
