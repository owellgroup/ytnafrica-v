package com.example.musicroyalties.services;

import com.example.musicroyalties.models.ArtistWork;
import com.example.musicroyalties.models.Status;
import com.example.musicroyalties.models.User;
import com.example.musicroyalties.repositories.ArtistWorkRepository;
import com.example.musicroyalties.repositories.StatusRepository;
import com.example.musicroyalties.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    
    @Autowired
    private ArtistWorkRepository artistWorkRepository;
    
    @Autowired
    private StatusRepository statusRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<ArtistWork> getPendingMusic() {
        Status pendingStatus = statusRepository.findByStatus(Status.EStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("Pending status not found"));
        return artistWorkRepository.findByStatusId(pendingStatus.getId());
    }
    
    public ArtistWork approveMusic(Long musicId, String isrcCode) {
        ArtistWork music = artistWorkRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("Music not found"));
        
        Status approvedStatus = statusRepository.findByStatus(Status.EStatus.APPROVED)
                .orElseGet(() -> {
                    Status status = new Status();
                    status.setStatus(Status.EStatus.APPROVED);
                    return statusRepository.save(status);
                });
        
        music.setStatus(approvedStatus);
        music.setISRC_code(isrcCode);
        
        ArtistWork savedMusic = artistWorkRepository.save(music);
        
        // Send approval email
        emailService.sendMusicApprovalEmail(music.getUser().getEmail());
        
        return savedMusic;
    }
    
    public ArtistWork rejectMusic(Long musicId, String notes) {
        ArtistWork music = artistWorkRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("Music not found"));
        
        Status rejectedStatus = statusRepository.findByStatus(Status.EStatus.REJECTED)
                .orElseGet(() -> {
                    Status status = new Status();
                    status.setStatus(Status.EStatus.REJECTED);
                    return statusRepository.save(status);
                });
        
        music.setStatus(rejectedStatus);
        music.setNotes(notes);
        
        ArtistWork savedMusic = artistWorkRepository.save(music);
        
        // Send rejection email
        emailService.sendMusicRejectionEmail(music.getUser().getEmail(), notes);
        
        return savedMusic;
    }
    
    public User createCompanyUser(String email, String password, String companyName, String companyAddress, 
                                 String companyPhone, String contactPerson, String companyEmail) {
        // Create user with encoded password
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.COMPANY);
        user.setEnabled(true);
        user.setEmailVerified(true);
        
        return userRepository.save(user);
    }

    //Update Company




    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setEnabled(userDetails.isEnabled());
        existingUser.setEmailVerified(userDetails.isEmailVerified());
        
        return userRepository.save(existingUser);
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
