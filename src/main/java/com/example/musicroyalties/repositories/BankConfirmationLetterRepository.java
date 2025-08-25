package com.example.musicroyalties.repositories;

import com.example.musicroyalties.models.BankConfirmationLetter;
import com.example.musicroyalties.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankConfirmationLetterRepository extends JpaRepository<BankConfirmationLetter, Long> {
    Optional<BankConfirmationLetter> findByUser(User user);
    Optional<BankConfirmationLetter> findByUserId(Long userId);
}
