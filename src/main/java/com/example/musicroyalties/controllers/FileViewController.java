package com.example.musicroyalties.controllers;

import com.example.musicroyalties.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class FileViewController {
    
    @Autowired
    private PassportPhotoService passportPhotoService;
    
    @Autowired
    private ProofOfPaymentService proofOfPaymentService;
    
    @Autowired
    private BankConfirmationLetterService bankConfirmationLetterService;
    
    @Autowired
    private IdDocumentService idDocumentService;
    
    @Autowired
    private MusicService musicService;
    
    // Passport Photo View
    @GetMapping("/api/passportphoto/view/{fileName:.+}")
    public ResponseEntity<Resource> viewPassportPhoto(@PathVariable String fileName) {
        try {
            Resource resource = passportPhotoService.loadFileAsResource(fileName);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Proof of Payment View
    @GetMapping("/api/proofofpayment/view/{fileName:.+}")
    public ResponseEntity<Resource> viewProofOfPayment(@PathVariable String fileName) {
        try {
            Resource resource = proofOfPaymentService.loadFileAsResource(fileName);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Bank Confirmation Letter View
    @GetMapping("/api/bankconfirmationletter/view/{fileName:.+}")
    public ResponseEntity<Resource> viewBankConfirmationLetter(@PathVariable String fileName) {
        try {
            Resource resource = bankConfirmationLetterService.loadFileAsResource(fileName);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // ID Document View
    @GetMapping("/api/iddocument/view/{fileName:.+}")
    public ResponseEntity<Resource> viewIdDocument(@PathVariable String fileName) {
        try {
            Resource resource = idDocumentService.loadFileAsResource(fileName);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Music File View
    @GetMapping("/api/music/view/{fileName:.+}")
    public ResponseEntity<Resource> viewMusicFile(@PathVariable String fileName) {
        try {
            Resource resource = musicService.loadFileAsResource(fileName);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Music Download
    @GetMapping("/api/music/download/{id}")
    public ResponseEntity<?> downloadMusic(@PathVariable Long id) {
        try {
            return musicService.downloadMusic(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Download failed: " + e.getMessage());
        }
    }
}
