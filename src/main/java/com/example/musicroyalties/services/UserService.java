package com.example.musicroyalties.services;

import com.example.musicroyalties.models.User;
import com.example.musicroyalties.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import for Forgot paassword
import java.time.LocalDateTime;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
    
    public User createArtist(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.ARTIST);
        user.setVerificationToken(UUID.randomUUID().toString());
        
        User savedUser = userRepository.save(user);
        emailService.sendVerificationEmail(email, savedUser.getVerificationToken());
        
        return savedUser;
    }
    
    public User createCompany(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.COMPANY);
        user.setEnabled(true);
        user.setEmailVerified(true);
        
        return userRepository.save(user);
    }
    //creating Admin,
    public User createAdmin(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.ADMIN);
        user.setEnabled(true);
        user.setEmailVerified(true);
        
        return userRepository.save(user);
    }
    
    public boolean verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElse(null);
        
        if (user != null && user.getVerificationToken().equals(token)) {
            user.setEmailVerified(true);
            user.setEnabled(true);
            user.setVerificationToken(null); // Clear the token after use
            userRepository.save(user);
            return true;
        }
        
        return false;
    }

    //get users
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //section to implement forgot password
    // Generate reset token and send email
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(1); // 1-hour expiry

        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(expiry);
        userRepository.save(user);

        emailService.sendPasswordResetEmail(email, token);
    }

    //section to implement forgot password2 for validation
    // Validate token and reset password
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        userRepository.save(user);
    }

}
