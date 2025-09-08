package com.example.musicroyalties.controllers;

import com.example.musicroyalties.models.*;
import com.example.musicroyalties.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/artist")
@CrossOrigin(origins = "*")
public class ArtistController {
    
    @Autowired
    private MemberDetailsService memberDetailsService;
    
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
    @Autowired
    private UserService userService;
    
    @PostMapping("/profile")
    public ResponseEntity<?> createProfile(@RequestBody MemberDetails memberDetails, 
                                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            MemberDetails savedProfile = memberDetailsService.createMemberDetails(memberDetails, user);
            return ResponseEntity.ok(savedProfile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Profile creation failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            MemberDetails profile = memberDetailsService.getMemberDetailsByUser(user);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Profile retrieval failed: " + e.getMessage());
        }
    }
    //if this does not work than I will use @AuthenticationPrincipal, But this is for Admin
    @PutMapping("/profile/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody MemberDetails memberDetails) {
        try {
            MemberDetails updatedProfile = memberDetailsService.updateMemberDetails(id, memberDetails);
            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Profile update failed: " + e.getMessage());
        }
    }

    //Updating for Users, why am i not using /profile/{id}
    @PutMapping("/profile")
    public ResponseEntity<?> updateMyProfile(@RequestBody MemberDetails memberDetails,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;

            // Fetch the current profile of the authenticated user
            MemberDetails existingProfile = memberDetailsService.getMemberDetailsByUser(user);

            // Update the existing profile with the new data
            MemberDetails updatedProfile = memberDetailsService.updateMemberDetails(existingProfile.getId(), memberDetails);

            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Profile update failed: " + e.getMessage());
        }
    }


    @PostMapping("/passport-photo")
    public ResponseEntity<?> uploadPassportPhoto(@RequestParam MultipartFile file,
                                               @RequestParam String imageTitle,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            PassportPhoto photo = passportPhotoService.postPhoto(file, imageTitle, user);
            return ResponseEntity.ok(photo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Passport photo upload failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/proof-of-payment")
    public ResponseEntity<?> uploadProofOfPayment(@RequestParam MultipartFile file,
                                                 @RequestParam String documentTitle,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            ProofOfPayment document = proofOfPaymentService.uploadDocument(file, documentTitle, user);
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Proof of payment upload failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/bank-confirmation-letter")
    public ResponseEntity<?> uploadBankConfirmationLetter(@RequestParam MultipartFile file,
                                                         @RequestParam String documentTitle,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            BankConfirmationLetter document = bankConfirmationLetterService.uploadDocument(file, documentTitle, user);
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bank confirmation letter upload failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/id-document")
    public ResponseEntity<?> uploadIdDocument(@RequestParam MultipartFile file,
                                             @RequestParam String documentTitle,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            IdDocument document = idDocumentService.uploadDocument(file, documentTitle, user);
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ID document upload failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/music/upload")
    public ResponseEntity<?> uploadMusic(@RequestParam MultipartFile file, @RequestParam String title, @AuthenticationPrincipal UserDetails userDetails, @RequestParam String ArtistId,
                                         @RequestParam String albumName,
                                         @RequestParam String artist,
                                         @RequestParam String GroupOrBandOrStageName,
                                         @RequestParam String featuredArtist,
                                         @RequestParam String producer,
                                         @RequestParam String country,
                                         //@RequestParam LocalDate uploadedDate,
                                         @RequestParam Long artistUploadTypeId,
                                         @RequestParam Long artistWorkTypeId,
                                         @RequestParam String Duration,
                                         @RequestParam String composer,
                                         @RequestParam String author,
                                         @RequestParam String arranger,
                                         @RequestParam String publisher,
                                         @RequestParam String publishersName,
                                         @RequestParam String publisherAdress,
                                         @RequestParam String publisherTelephone,
                                         @RequestParam String recordedBy,
                                         @RequestParam String AddressOfRecordingCompany,
                                         @RequestParam String labelName,
                                         @RequestParam String dateRecorded) {
        try {
            User user = (User) userDetails;
            ArtistWork music = musicService.uploadMusic(file, title, user, ArtistId, albumName, artist, GroupOrBandOrStageName, featuredArtist, producer,country, artistUploadTypeId, artistWorkTypeId, Duration, composer, author, arranger, publisher, publishersName, publisherAdress,publisherTelephone,recordedBy, AddressOfRecordingCompany, labelName, dateRecorded );
            return ResponseEntity.ok(music);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Music upload failed: " + e.getMessage());
        }
    }
    //for users
    @GetMapping("/music")
    public ResponseEntity<?> getMyMusic(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            return ResponseEntity.ok(musicService.getMusicByUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Music retrieval failed: " + e.getMessage());
        }
    }
    //most used by admin
    //if this does not work than I might remove aunthentication or not to get users documents, where admin or user
    @GetMapping("/documentsandprofile")
    public ResponseEntity<?> getMyDocuments(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            Map<String, Object> documents = new HashMap<>();
            
            // Get all document types for the user
            documents.put("passportPhoto", passportPhotoService.getByUserId(user.getId()));
            documents.put("proofOfPayment", proofOfPaymentService.getByUserId(user.getId()));
            documents.put("bankConfirmationLetter", bankConfirmationLetterService.getByUserId(user.getId()));
            documents.put("idDocument", idDocumentService.getByUserId(user.getId()));
            //recently added to get profile for user
            documents.put("memberDetails",memberDetailsService.getByUserId(user.getId()));
            
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Documents retrieval failed: " + e.getMessage());
        }
    }

    //the rest of the Controllers more like for Admin
    //Get All members
    @GetMapping("/all")
    public List<MemberDetails> getAllMembers(){
        return memberDetailsService.getAllMemberDetails();
    }

    //Get Members by id
    @GetMapping("/{id}")
    public Optional<MemberDetails> getMemberDetails(@PathVariable Long id){
        return memberDetailsService.getMemberDetailsById(id);
    }

    //Delete
    @DeleteMapping("/delete/{id}")
    public void deleteMemberDetails(@PathVariable Long id){
        memberDetailsService.deleteMemberDetailsById(id);
    }

  //get Users by emails
    @GetMapping("/getartist")
    public ResponseEntity<?> getArtist(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            return ResponseEntity.ok(userService.getUserByEmail(user.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }





}
