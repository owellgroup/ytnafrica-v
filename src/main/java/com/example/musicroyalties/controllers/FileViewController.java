package com.example.musicroyalties.controllers;

import com.example.musicroyalties.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    //new Passport view
    @GetMapping("/api/passportphoto/view/{fileName:.+}")
    public ResponseEntity<Resource> viewPassportPhoto(@PathVariable String fileName, HttpServletRequest request) {
        try {
            Resource resource = passportPhotoService.loadFileAsResource(fileName);

            // Detect content type (PNG, JPEG, etc.)
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream"; // fallback
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    // Proof of Payment View
    @GetMapping("/api/proofofpayment/view/{fileName:.+}")
    public ResponseEntity<Resource> viewProofOfPayment(@PathVariable String fileName,
                                                       HttpServletRequest request) {
        try {
            Resource resource = proofOfPaymentService.loadFileAsResource(fileName);

            // Detect file content type (image/jpeg, image/png, application/pdf, etc.)
            String contentType = request.getServletContext()
                    .getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream"; // fallback
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    //New Bank Comfirmation letter View
    @GetMapping("/api/bankconfirmationletter/view/{fileName:.+}")
    public ResponseEntity<Resource> viewBankConfirmationLetter(@PathVariable String fileName,
                                                               HttpServletRequest request) {
        try {
            Resource resource = bankConfirmationLetterService.loadFileAsResource(fileName);

            // Detect file content type
            String contentType = request.getServletContext()
                    .getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream"; // fallback
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ID Document View
    @GetMapping("/api/iddocument/view/{fileName:.+}")
    public ResponseEntity<Resource> viewIdDocument(@PathVariable String fileName,
                                                   HttpServletRequest request) {
        try {
            Resource resource = idDocumentService.loadFileAsResource(fileName);

            // Detect file content type
            String contentType = request.getServletContext()
                    .getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream"; // fallback
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }




//    // Music File View
//    @GetMapping("/api/music/view/{fileName:.+}")
//    public ResponseEntity<Resource> viewMusicFile(@PathVariable String fileName) {
//        try {
//            Resource resource = musicService.loadFileAsResource(fileName);
//            return ResponseEntity.ok()
//                    .header("Content-Disposition", "inline; filename=\"" + resource.getFilename() + "\"")
//                    .body(resource);
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    // Music File View
    @GetMapping("/api/music/view/{fileName:.+}")
    public ResponseEntity<Resource> viewMusicFile(@PathVariable String fileName,
                                                  HttpServletRequest request) {
        try {
            Resource resource = musicService.loadFileAsResource(fileName);

            // Detect file content type
            String contentType = request.getServletContext()
                    .getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream"; // fallback
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    // Music Download
//    @GetMapping("/api/music/download/{id}")
//    public ResponseEntity<?> downloadMusic(@PathVariable Long id) {
//        try {
//            return musicService.downloadMusic(id);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Download failed: " + e.getMessage());
//        }
//    }
    // Music Download
//    @GetMapping("/api/music/download/{fileName:.+}")
//    public ResponseEntity<Resource> downloadMusic(@PathVariable String fileName,
//                                                  HttpServletRequest request) {
//        try {
//            Resource resource = musicService.loadFileAsResource(fileName);
//
//            // Detect MIME type
//            String contentType = request.getServletContext()
//                    .getMimeType(resource.getFile().getAbsolutePath());
//            if (contentType == null) {
//                contentType = "application/octet-stream";
//            }
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            "attachment; filename=\"" + resource.getFilename() + "\"")
//                    .body(resource);
//
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    //forcely Download
    @GetMapping("/api/music/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadMusic(@PathVariable String fileName,
                                                  HttpServletRequest request) {
        try {
            // Load file as resource
            Resource resource = musicService.loadFileAsResource(fileName);

            if (resource == null || !resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Detect MIME type
            String contentType = request.getServletContext()
                    .getMimeType(resource.getFile().getAbsolutePath());

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    // Force download with the original filename
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }








    //downloading endpoints, Use this end points
    // Passport Photo Download
    @GetMapping("/api/passportphoto/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadPassportPhoto(@PathVariable String fileName,
                                                          HttpServletRequest request) {
        try {
            Resource resource = passportPhotoService.loadFileAsResource(fileName);

            // Detect MIME type
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Proof of Payment Download
    @GetMapping("/api/proofofpayment/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadProofOfPayment(@PathVariable String fileName,
                                                           HttpServletRequest request) {
        try {
            Resource resource = proofOfPaymentService.loadFileAsResource(fileName);

            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Bank Confirmation Letter Download
    @GetMapping("/api/bankconfirmationletter/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadBankConfirmationLetter(@PathVariable String fileName,
                                                                   HttpServletRequest request) {
        try {
            Resource resource = bankConfirmationLetterService.loadFileAsResource(fileName);

            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ID Document Download
    @GetMapping("/api/iddocument/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadIdDocument(@PathVariable String fileName,
                                                       HttpServletRequest request) {
        try {
            Resource resource = idDocumentService.loadFileAsResource(fileName);

            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
