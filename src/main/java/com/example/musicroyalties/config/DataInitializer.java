package com.example.musicroyalties.config;

import com.example.musicroyalties.models.Status;
import com.example.musicroyalties.models.User;
import com.example.musicroyalties.repositories.StatusRepository;
import com.example.musicroyalties.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private StatusRepository statusRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        initializeStatuses();
        initializeAdminUser();
    }
    
    private void initializeStatuses() {
        // Create PENDING status if it doesn't exist
        if (statusRepository.findByStatus(Status.EStatus.PENDING).isEmpty()) {
            Status pendingStatus = new Status();
            pendingStatus.setStatus(Status.EStatus.PENDING);
            statusRepository.save(pendingStatus);
        }
        
        // Create APPROVED status if it doesn't exist
        if (statusRepository.findByStatus(Status.EStatus.APPROVED).isEmpty()) {
            Status approvedStatus = new Status();
            approvedStatus.setStatus(Status.EStatus.APPROVED);
            statusRepository.save(approvedStatus);
        }
        
        // Create REJECTED status if it doesn't exist
        if (statusRepository.findByStatus(Status.EStatus.REJECTED).isEmpty()) {
            Status rejectedStatus = new Status();
            rejectedStatus.setStatus(Status.EStatus.REJECTED);
            statusRepository.save(rejectedStatus);
        }
    }
    
    private void initializeAdminUser() {
        // Create admin user if it doesn't exist
        if (userRepository.findByEmail("admin@musicroyalties.com").isEmpty()) {
            User adminUser = new User();
            adminUser.setEmail("admin@musicroyalties.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(User.Role.ADMIN);
            adminUser.setEnabled(true);
            adminUser.setEmailVerified(true);
            userRepository.save(adminUser);
            
            System.out.println("Admin user created: admin@musicroyalties.com / admin123");
        }
    }
}
