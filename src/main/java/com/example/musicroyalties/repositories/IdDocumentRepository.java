package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.IdDocument;
import com.example.musicroyalties.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdDocumentRepository extends JpaRepository<IdDocument, Long> {
    Optional<IdDocument> findByUser(User user);
    Optional<IdDocument> findByUserId(Long userId);
}
