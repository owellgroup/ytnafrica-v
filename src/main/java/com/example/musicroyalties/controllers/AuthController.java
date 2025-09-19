package com.example.musicroyalties.controllers;

import com.example.musicroyalties.models.User;
import com.example.musicroyalties.services.JwtService;
import com.example.musicroyalties.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @PostMapping("/register/artist")
    public ResponseEntity<?> registerArtist(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            User user = userService.createArtist(email, password);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Artist registered successfully. Please check your email for verification.");
            response.put("userId", user.getId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/register/company")
    public ResponseEntity<?> registerCompany(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            User user = userService.createCompany(email, password);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Company registered successfully.");
            response.put("userId", user.getId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
            
            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);
            response.put("message", "Login successful");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }
    //
//    @GetMapping("/verify")
//    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
//        try {
//            boolean verified = userService.verifyEmail(token);
//            if (verified) {
//                return ResponseEntity.ok("Email verified successfully. You can now login.");
//            } else {
//                return ResponseEntity.badRequest().body("Email verification failed.");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Verification failed: " + e.getMessage());
//        }
//    }

    //end point for the very
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        try {
            boolean verified = userService.verifyEmail(token);
            if (verified) {
                String loginUrl = "https://namsa.vercel.app"; // replace this
                String logoUrl = "https://namsa.vercel.app/assets/namsa-logo-BGptgL6M.png";

                String htmlResponse =
                        "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #f0f0f0; border-radius: 10px; background-color: #f9f7ff;'>" +
                                "<div style='text-align: center; padding-bottom: 20px; border-bottom: 2px solid #ed7c08;'>" +
                                "<img src='" + logoUrl + "' alt='YTN Africa Logo' style='height: 100px; object-fit: contain;' />" +
                                "</div>" +

                                "<div style='padding: 30px; color: #444; line-height: 1.6;'>" +
                                "<h2 style='color: #ed7c08; margin-top: 0;'>Email Verified Successfully</h2>" +
                                "<p style='font-size: 16px; color: #333;'>Your email has been verified successfully. You can now login to your account.</p>" +

                                "<div style='text-align: center; margin: 30px 0;'>" +
                                "<a href='" + loginUrl + "' " +
                                "style='background-color: #ed7c08; color: white; padding: 14px 28px; text-decoration: none; font-size: 16px; border-radius: 6px; display: inline-block; font-weight: bold;'>" +
                                "Click to Login" +
                                "</a>" +
                                "</div>" +

                                "<p style='font-size: 14px; color: #666;'>If the button doesn't work, copy and paste the following link into your browser:</p>" +
                                "<p style='font-size: 14px; word-break: break-all; color: #ed7c08;'>" + loginUrl + "</p>" +
                                "</div>" +

                                "<div style='text-align: center; padding-top: 20px; border-top: 1px solid #eaeaea; color: #999; font-size: 13px;'>" +
                                "<p>&copy; 2025 namsa. All rights reserved.</p>" +
                                "<p>For support, contact us at <a href='mailto:support@namsa.com' style='color: #8a2be2; text-decoration: none;'>support@namsa.com</a></p>" +
                                "</div>" +
                                "</div>";

                return ResponseEntity.ok()
                        .header("Content-Type", "text/html")
                        .body(htmlResponse);
            } else {
                return ResponseEntity.badRequest().body("Email verification failed.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Verification failed: " + e.getMessage());
        }
    }


    //endpoint for forgot password
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            userService.initiatePasswordReset(email);
            return ResponseEntity.ok("Password reset link has been sent to your email.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    //endpoint for reset password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password has been reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


}
