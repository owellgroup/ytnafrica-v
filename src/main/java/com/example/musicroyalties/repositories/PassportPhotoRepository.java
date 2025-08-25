package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.PassportPhoto;
import com.example.musicroyalties.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassportPhotoRepository extends JpaRepository<PassportPhoto, Long> {
    Optional<PassportPhoto> findByUser(User user);
    Optional<PassportPhoto> findByUserId(Long userId);
}
