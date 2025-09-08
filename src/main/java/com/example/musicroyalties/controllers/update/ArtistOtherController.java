package com.example.musicroyalties.controllers.update;

import com.example.musicroyalties.models.*;
import com.example.musicroyalties.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/artist2/")
@CrossOrigin(origins = "*")

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
    public ArtistWork updamusic(Long id,@AuthenticationPrincipal UserDetails userDetails, @RequestParam MultipartFile file, @RequestParam String title, @RequestParam String ArtistId,
                                @RequestParam String albumName,
                                @RequestParam String artist,

                                @RequestParam String GroupOrBandOrStageName,
                                @RequestParam String featuredArtist,
                                @RequestParam String producer,
                                @RequestParam String country,

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
                                @RequestParam String dateRecorded) throws Exception {
        User user = (User) userDetails;
        return musicService.updateMusic(id,file, title, ArtistId, albumName, artist, GroupOrBandOrStageName, featuredArtist, producer,country, artistUploadTypeId, artistWorkTypeId, Duration, composer, author, arranger, publisher, publishersName, publisherAdress,publisherTelephone,recordedBy, AddressOfRecordingCompany, labelName, dateRecorded );
    }
// End of the Part for the Admin Us
// This Part is for Aunthenticated users to
// Update passport photo (for authenticated user

//Logged in Users//passport
@PutMapping("/updatephotobyuser")
public ResponseEntity<?> updatePassportPhoto(@PathVariable@RequestParam("file") MultipartFile file,
                                             @RequestParam("imageTitle") String imageTitle,
                                             @AuthenticationPrincipal UserDetails userDetails) {
    try {
        User user = (User) userDetails;

        // Fetch the photo for this user
        PassportPhoto existingPhoto = passportPhotoService.getByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("You don't have a passport photo yet"));

        // Update the photo

        // PassportPhoto updatedPhoto = passportPhotoService.updatePhoto(existingPhoto.getId(), file, String.valueOf(id));
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

            // fetch proof of payment
            ProofOfPayment existingproof = proofOfPaymentService.getByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("You don't have a proof of payment"));

            // Update Proof of pay
            ProofOfPayment updatedpayment = proofOfPaymentService.updateDocument(existingproof.getId(), file, documentTitle);
            return ResponseEntity.ok(updatedpayment);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("proof of payment update failed: " + e.getMessage());
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

    public ResponseEntity<?> bankconfrimletter (@RequestParam("file") MultipartFile file,
                                         @RequestParam("documentTitle") String documentTitle,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;

            // Fetch the bank comfirm letter for this user
            BankConfirmationLetter exsitingbank = bankConfirm.getByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("You don't have a proof of payment"));

            // Update the bank Comfirm
            BankConfirmationLetter updatedpayment = bankConfirm.updateDocument(exsitingbank.getId(), file, documentTitle);
            return ResponseEntity.ok(updatedpayment);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Proof of payment update failed: " + e.getMessage());
        }
    }


    //    @PutMapping("/updatebankletteruser")
//    public ResponseEntity<?> updateBankLetterUser(@RequestParam("file") MultipartFile file,@RequestParam("documentTitle") String documentTitle, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
//        User user = (User) userDetails;
//        BankConfirmationLetter existingBankC = bankConfirm.getByUserId(user.getId()).orElseThrow(() -> new  RuntimeException("You don't have a passport photo yet"));
//        BankConfirmationLetter updated = bankConfirm.updateDocument(existingBankC.getId(), file, documentTitle);
//        return ResponseEntity.ok(updated);
//    }
    //Delete bank Comfirm letter
    @DeleteMapping("/deletebankletteruser")
    public  ResponseEntity<?> deleteBankLetterUser(@AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User user = (User) userDetails;
        BankConfirmationLetter exsitingone = bankConfirm.getByUserId(user.getId()).orElseThrow(() -> new  RuntimeException("You don't have a passport photo yet"));
        bankConfirm.delete(exsitingone.getId());
        return ResponseEntity.ok("Bank confirmation deleted successfully");

    }

    //Update Id, not sure abiiut id
//    @PutMapping("/updatiddocbyuser")
//    public ResponseEntity<?> updateUserId (@RequestParam("file") MultipartFile file,@RequestParam("documentTitle") String documentTitle, @AuthenticationPrincipal UserDetails userDetails) throws Exception {
//        User user = (User) userDetails;
//        IdDocument existing = idDocumentService.getByUserId(user.getId()).orElseThrow(() -> new RuntimeException("You don't have a passport photo yet"));
//        IdDocument updated = idDocumentService.updateDocument(existing.getId(), file, documentTitle);
//        return ResponseEntity.ok(updated);
//    }

    //new Update for ID
    @PutMapping("/updatiddocbyuser")

    public ResponseEntity<?> udateIdDoc (@RequestParam("file") MultipartFile file,
                                                @RequestParam("documentTitle") String documentTitle,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = (User) userDetails;

            // Fetch the bank comfirm letter for this user
            IdDocument exsitingbank = idDocumentService.getByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("You don't have a ID"));

            // Update the bank Comfirm
            IdDocument updatedpayment = idDocumentService.updateDocument(exsitingbank.getId(), file, documentTitle);
            return ResponseEntity.ok(updatedpayment);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ID update failed: " + e.getMessage());
        }
    }


    //delete
    @DeleteMapping("/deleteuseriddoc")
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

//    //Music side
//    @PutMapping("/updatemusicbyuser/{id}")
//    public ResponseEntity<?> updatems (@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, @RequestParam MultipartFile file, @RequestParam String title, @RequestParam String ArtistId,
//                                       @RequestParam String albumName,
//                                       @RequestParam String artist,
//                                       @RequestParam String GroupOrBandOrStageName,
//                                       @RequestParam String featuredArtist,
//                                       @RequestParam String producer,
//                                       @RequestParam String country,
//                                       //@RequestParam LocalDate uploadedDate,
//                                       @RequestParam Long artistUploadTypeId,
//                                       @RequestParam Long artistWorkTypeId,
//                                       @RequestParam String Duration,
//                                       @RequestParam String composer,
//                                       @RequestParam String author,
//                                       @RequestParam String arranger,
//                                       @RequestParam String publisher,
//                                       @RequestParam String publishersName,
//                                       @RequestParam String publisherAdress,
//                                       @RequestParam String publisherTelephone,
//                                       @RequestParam String recordedBy,
//                                       @RequestParam String AddressOfRecordingCompany,
//                                       @RequestParam String labelName,
//                                       @RequestParam String dateRecorded) {
//
//        try {
//            User user = (User) userDetails;
//            ArtistWork exist = musicService.getByUserId(user.getId()).orElseThrow(() -> new RuntimeException("You don't have a music   yet"));
//            ArtistWork musicId = musicService.getMusicById(exist.getId()).orElseThrow(() -> new RuntimeException("You don't have a music"));
//            ArtistWork update = musicService.updateMusic(exist.getId(), musicId.getId(), file, title, ArtistId, albumName, GroupOrBandOrStageName, artist, featuredArtist, producer, country, artistUploadTypeId, artistWorkTypeId, Duration, composer, author, arranger, publisher, publishersName, publisherAdress, publisherTelephone, recordedBy, AddressOfRecordingCompany, labelName, dateRecorded );
//            return ResponseEntity.ok(update);
//        }catch (Exception e) {
//            return ResponseEntity.badRequest().body("Music Update failed: " + e.getMessage());
//        }
//    }
    //New update
@PutMapping("/updatemusicbyuser/{id}")
public ResponseEntity<?> updateMusicByUser(
        @PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam MultipartFile file,
        @RequestParam String title,
        @RequestParam String ArtistId,
        @RequestParam String albumName,
        @RequestParam String artist,
        @RequestParam String GroupOrBandOrStageName,
        @RequestParam String featuredArtist,
        @RequestParam String producer,
        @RequestParam String country,
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
        // get logged in user
        User currentUser = (User) userDetails;

        // find the music by id
        ArtistWork music = musicService.getMusicById(id)
                .orElseThrow(() -> new RuntimeException("Music not found with id: " + id));

        // check ownership
        if (!music.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own music");
        }

        // perform update
        ArtistWork updated = musicService.updateMusic(
                id,
                file,
                title,
                ArtistId,
                albumName,
                GroupOrBandOrStageName,
                artist,
                featuredArtist,
                producer,
                country,
                artistUploadTypeId,
                artistWorkTypeId,
                Duration,
                composer,
                author,
                arranger,
                publisher,
                publishersName,
                publisherAdress,
                publisherTelephone,
                recordedBy,
                AddressOfRecordingCompany,
                labelName,
                dateRecorded
        );

        return ResponseEntity.ok(updated);

    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Music Update failed: " + e.getMessage());
    }
}


    //delete
    @DeleteMapping("/deletemusicbyuserid/{id}")
    public ResponseEntity<?> deletemusic (@AuthenticationPrincipal UserDetails userDetails,@PathVariable Long id) throws Exception {
        User user = (User) userDetails;
        ArtistWork music = musicService.getMusicById(id)
                .orElseThrow(() -> new RuntimeException("Music not found with id: " + id));
      musicService.deleteMusic(id);
      return ResponseEntity.ok(music);

    }

}
