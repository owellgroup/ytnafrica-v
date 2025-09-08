package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.IdDocument;
import com.example.musicroyalties.models.MemberDetails;
import com.example.musicroyalties.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberDetailsRepository extends JpaRepository<MemberDetails, Long> {
    Optional<MemberDetails> findByUser(User user);
    Optional<MemberDetails> findByUserId(Long userId);
    List<MemberDetails> findByStatusId(Long statusId);
}
