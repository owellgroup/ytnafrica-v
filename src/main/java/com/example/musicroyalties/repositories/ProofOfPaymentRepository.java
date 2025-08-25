package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.ProofOfPayment;
import com.example.musicroyalties.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProofOfPaymentRepository extends JpaRepository<ProofOfPayment, Long> {
    Optional<ProofOfPayment> findByUser(User user);
    Optional<ProofOfPayment> findByUserId(Long userId);
}
