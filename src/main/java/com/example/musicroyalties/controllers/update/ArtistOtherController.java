package com.example.musicroyalties.controllers.update;

import com.example.musicroyalties.models.*;
import com.example.musicroyalties.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/artist2/")

public class ArtistOtherController {
    @Autowired
    private PassportPhotoService passportPhotoService;
    @Autowired
    private IdDocumentService idDocumentService;
    @Autowired
    private BankConfirmationLetterService bankConfirm;
    @Autowired
    private ProofOfPaymentService proofOfPaymentService;
    @Autowired
    private MusicService musicService;

    //This Part is for Admin Use only if neccesary
    //delete for passport
    @DeleteMapping("/deletephoto/{id}")
    public void deleteById(@PathVariable("id") Long id) throws Exception {
        passportPhotoService.delete(id);
    }

    //Update for passport
    @PutMapping("/updatephoto/{id}")
    public PassportPhoto updae(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id, @RequestParam("imageTitle") String imageTitle) throws Exception {
        return passportPhotoService.updatePhoto(id, file, imageTitle);
    }

    //delete for id docs
    @DeleteMapping("/deleteid/{id}")
    public void deleteIdById(@PathVariable("id") Long id) throws Exception {
        idDocumentService.delete(id);
    }

    //update Documents
    @PutMapping("/updateid/{id}")
    public IdDocument updateid(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id, @RequestParam("documentTitle") String documentTitle) throws Exception {
        return idDocumentService.updateDocument(id, file, documentTitle);
    }

    //delete bank Comfimation Letter
    @DeleteMapping("/deletebankconfirm/{id}")
    public void deleteBankConfirm(@PathVariable("id") Long id) throws Exception {
        bankConfirm.delete(id);
    }

    //update Bank Confirm letter
    @PutMapping("/updatebankconfirm/{id}")
    public BankConfirmationLetter updateb(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id, @RequestParam("documentTitle") String documentTitle) throws Exception {
        return bankConfirm.updateDocument(id, file, documentTitle);
    }

    //Delete for ProofOfpayment
    @DeleteMapping("/deleteProofOfpayment/{id}")
    public void deleteProofOfpayment(@RequestParam("id") Long id) throws Exception {
        proofOfPaymentService.delete(id);
    }

    // Update proof
    @PutMapping("/updateproof/{id}")
    public ProofOfPayment updateProof(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id, @RequestParam("documentTitle") String documentTitle) throws Exception {
        return proofOfPaymentService.updateDocument(id, file, documentTitle);
    }

    //Music Side yet to be updated
    //Delete Music
    @DeleteMapping("/deletemusic/{id}")
    public void deleteMusic(@RequestParam("id") Long id) throws Exception {
        musicService.deleteMusic(id);
    }

    @PutMapping("/updatemusic/{id}")
    public ArtistWork updamusic(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id, @RequestParam("documentTitle") String documentTitle) throws Exception {
        return musicService.updateMusic(id, file, documentTitle);
    }
// End of the Part for the Admin Us
// This Part is for Aunthenticated users to
// Update passport photo (for authenticated user

//Logged in Users//passport
@PutMapping("/updatephotobyuser")
public ResponseEntity<?> updatePassportPhoto(@RequestParam("file") MultipartFile file,
                                             @RequestParam("imageTitle") String imageTitle,
                                             @AuthenticationPrincipal UserDetails userDetails) {
    try {
        User user = (User) userDetails;

        // Fetch the photo for this user
        PassportPhoto existingPhoto = passportPhotoService.getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("You don't have a passport photo yet"));

        // Update the photo
        PassportPhoto updatedPhoto = passportPhotoService.updatePhoto(existingPhoto.getId(), file, imageTitle);
        return ResponseEntity.ok(updatedPhoto);

    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Passport photo update failed: " + e.getMessage());
    }
}

    // Delete passport photo (for authenticated user)
    @DeleteMapping("/deleteuserphoto")
    public ResponseEntity<?> deletePassportPhoto(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;

            // Fetch the photo for this user
            PassportPhoto existingPhoto = passportPhotoService.getByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("You don't have a passport photo to delete"));

            // Delete the photo
            passportPhotoService.delete(existingPhoto.getId());
            return ResponseEntity.ok("Passport photo deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Passport photo deletion failed: " + e.getMessage());
        }
    }
    //update proof of payment
    @PutMapping("/updateproofofpayuser")
    public ResponseEntity<?> proofOfPay (@RequestParam("file") MultipartFile file,
                                                 @RequestParam("documentTitle") String documentTitle,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;

            // Fetch the photo for this user
            ProofOfPayment existingproof = proofOfPaymentService.getByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("You don't have a passport photo yet"));

            // Update the photo
            ProofOfPayment updatedpayment = proofOfPaymentService.updateDocument(existingproof.getId(), file, documentTitle);
            return ResponseEntity.ok(updatedpayment);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Passport photo update failed: " + e.getMessage());
        }
    }

    //Delete the proof of payment
    @DeleteMapping("/deleteproofofpay")
    public  ResponseEntity<?> deleteProofOfPay (@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            ProofOfPayment existingproof = proofOfPaymentService.getByUserId(user.getId()).orElseThrow(() -> new  RuntimeException("You don't have a passport photo yet"));
            proofOfPaymentService.delete(existingproof.getId());
            return ResponseEntity.ok("Proof of payment deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Proof of payment deletion failed: " + e.getMessage());
        }
    }

    //update Bank Comfirm letter
    @PutMapping("/updatebankletteruser")
    public ResponseEntity<?> updateBankLetterUser(@RequestParam("file") MultipartFile file,@RequestParam("documentTitle") String documentTitle, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        BankConfirmationLetter existingBankC = bankConfirm.getByUserId(user.getId()).orElseThrow(() -> new  RuntimeException("You don't have a passport photo yet"));
        BankConfirmationLetter updated = bankConfirm.updateDocument(existingBankC.getId(), file, documentTitle);
        return ResponseEntity.ok(updated);
    }
    //Delete bank Comfirm letter
    @DeleteMapping("/deletebankletteruser")
    public  ResponseEntity<?> deleteBankLetterUser(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        BankConfirmationLetter exsitingone = bankConfirm.getByUserId(user.getId()).orElseThrow(() -> new  RuntimeException("You don't have a passport photo yet"));
        bankConfirm.delete(exsitingone.getId());
        return ResponseEntity.ok("Bank confirmation deleted successfully");

    }

    //Update Id
    @PutMapping("/updateuserid")
    public ResponseEntity<?> updateUserId (@RequestParam("file") MultipartFile file,@RequestParam("documentTitle") String documentTitle, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        IdDocument existing = idDocumentService.getByUserId(user.getId()).orElseThrow(() -> new RuntimeException("You don't have a passport photo yet"));
        IdDocument updated = idDocumentService.updateDocument(existing.getId(), file, documentTitle);
        return ResponseEntity.ok(updated);
    }
    //delete
    @DeleteMapping("/deleteuserid")
    public  ResponseEntity<?> deleteUserId (@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        IdDocument exist= idDocumentService.getByUserId(user.getId()).orElseThrow(() -> new RuntimeException("You don't have a passport photo yet"));
        idDocumentService.delete(exist.getId());
        return ResponseEntity.ok("Id document deleted successfully");
    }

    //end of Authenticated user, delete and update

    //get Documents individually by Authenticated users
    //get for Passport
    @GetMapping("/getpassportuser")
    public ResponseEntity<?> getPassportUser(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        return ResponseEntity.ok(proofOfPaymentService.getByUserId(user.getId()));
    }
    //get for Id
    @GetMapping("/getiddocument")
    public ResponseEntity<?> getIdDocument(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        return ResponseEntity.ok(idDocumentService.getByUserId(user.getId()));
    }
    //get for bank comfirm letter
    @GetMapping("/getbankletter")
    public ResponseEntity<?> getBankLetterUser(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        return ResponseEntity.ok(bankConfirm.getByUserId(user.getId()));
    }

    //get proof of payment
    @GetMapping("/getproofofpay")
    public ResponseEntity<?> getProofOfPay(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        return ResponseEntity.ok(proofOfPaymentService.getByUserId(user.getId()));
    }

}
