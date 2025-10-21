package com.example.musicroyalties.services;

import com.example.musicroyalties.models.*;
import com.example.musicroyalties.repositories.ArtistWorkRepository;
import com.example.musicroyalties.repositories.StatusRepository;
import com.example.musicroyalties.services.lookupservices.ArtistUploadTypeService;
import com.example.musicroyalties.services.lookupservices.ArtistWorkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MusicService {
    
    @Autowired
    private ArtistWorkRepository artistWorkRepository;
    
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ArtistUploadTypeService artistUploadTypeService;
    @Autowired
    private ArtistWorkTypeService artistWorkTypeService;

    
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/music/";
    //not sure about the memberId and status
    public ArtistWork uploadMusic(MultipartFile file, String title, User user, String ArtistId, String albumName, String artist, String GroupOrBandOrStageName, String featuredArtist, String producer, String country, Long artistUploadTypeId, Long artistWorkTypeId, String Duration, String composer, String author, String arranger, String publisher, String publishersName, String publisherAdress, String publisherTelephone, String recordedBy, String AddressOfRecordingCompany, String labelName, String dateRecorded ) throws Exception {
        String contentType = file.getContentType();

        //calling Ids
        ArtistUploadType artistUploadType = artistUploadTypeService.getArtistUploadTypeById(artistUploadTypeId).orElseThrow(() -> new RuntimeException("uploadtype not found "));
        ArtistWorkType art = artistWorkTypeService.findWorkType(artistWorkTypeId).orElseThrow(() -> new RuntimeException("uploadtype not found "));
        //custom ID Generation
        String contentId = generateContentId(title, ArtistId);

        
        // Allow only audio and video files
        if (contentType == null || (!contentType.startsWith("audio/") && !contentType.startsWith("video/"))) {
            throw new IllegalArgumentException("Only audio and video files are allowed");
        }
        
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        
        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                + "_" + System.currentTimeMillis()
                + originalFileName.substring(originalFileName.lastIndexOf('.'));
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        String fileUrl = "https://api.owellserver.ggff.net/api/music/view/" + fileName;
        //String fileUrl = "http://localhost:8080/api/music/view/" + fileName;
        
        // Set default status to PENDING
        Status pendingStatus = statusRepository.findByStatus(Status.EStatus.PENDING)
                .orElseGet(() -> {
                    Status status = new Status();
                    status.setStatus(Status.EStatus.PENDING);
                    return statusRepository.save(status);
                });
        
        ArtistWork music = new ArtistWork();
        music.setRecordingCompanyTelephone(publisherTelephone);
        music.setLabelName(labelName);
        //music.setUploadedDate(uploadedDate);
        music.setDateRecorded(dateRecorded);
        music.setWorkId(contentId);
        music.setArranger(arranger);
        music.setPublishersName(publishersName);
        music.setArtist(artist);
        music.setAuthor(author);
        music.setAlbumName(albumName);
        music.setArtistId(ArtistId);
        music.setFeaturedArtist(featuredArtist);
        music.setProducer(producer);
        music.setCountry(country);
        music.setDuration(Duration);
        music.setComposer(composer);
        music.setPublisher(publisher);
        music.setPublisherAdress(publisherAdress);
        music.setPublisherTelephone(publisherTelephone);
        music.setRecordedBy(recordedBy);
        music.setAddressOfRecordingCompany(AddressOfRecordingCompany);
        music.setGroupOrBandOrStageName(GroupOrBandOrStageName);
        music.setTitle(title);
        music.setFileUrl(fileUrl);
        music.setFileType(contentType);
        music.setArtistUploadType(artistUploadType);
        music.setArtistWorkType(art);
        music.setUploadedDate(LocalDate.now());
        music.setUser(user);
        //not sure if it works
        //music.setIpiNumber(music.getIpiNumber());

        music.setStatus(pendingStatus);
        
        return artistWorkRepository.save(music);
    }
    
    public Resource loadFileAsResource(String fileName) throws MalformedURLException {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(fileName).normalize();
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            if (!filePath.startsWith(uploadPath)) {
                throw new RuntimeException("Invalid file path - attempted directory traversal");
            }
            
            if (!Files.exists(filePath)) {
                throw new RuntimeException("File not found: " + fileName);
            }
            
            if (!Files.isReadable(filePath)) {
                throw new RuntimeException("File is not readable: " + fileName);
            }
            
            Resource resource = new UrlResource(filePath.toUri());
            return resource;
            
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed file URL for: " + fileName + ". Error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load file: " + fileName + ". Error: " + e.getMessage());
        }
    }
    
    public ResponseEntity<Resource> downloadMusic(Long id) throws IOException {
        ArtistWork music = artistWorkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));
        
        Resource resource = loadFileAsResource(
            music.getFileUrl().substring(music.getFileUrl().lastIndexOf("/") + 1)
        );
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    //for specific users to get thier details only
    public List<ArtistWork> getMusicByUser(User user) {
        return artistWorkRepository.findByUser(user);
    }
    
    public List<ArtistWork> getApprovedMusic() {
        Status approvedStatus = statusRepository.findByStatus(Status.EStatus.APPROVED)
                .orElseThrow(() -> new RuntimeException("Approved status not found"));
        return artistWorkRepository.findByStatusId(approvedStatus.getId());
    }

    //get All music
    public List<ArtistWork> getAllmusic() {
        return  artistWorkRepository.findAll();
    }



    //Admin
    public Optional<ArtistWork> getMusicById(Long id) {
        return artistWorkRepository.findById(id);
    }
    //Get music by User Id
    public Optional<ArtistWork> getByUserId (Long userId) {
        return artistWorkRepository.findById(userId);
    }
    
    public ArtistWork updateMusic(Long id, MultipartFile file, String title, String ArtistId, String albumName, String artist, String GroupOrBandOrStageName, String featuredArtist, String producer, String country, Long artistUploadTypeId, Long artistWorkTypeId, String Duration, String composer, String author, String arranger, String publisher, String publishersName, String publisherAdress, String publisherTelephone, String recordedBy, String AddressOfRecordingCompany, String labelName, String dateRecorded ) throws IOException {
        ArtistWork music = artistWorkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));

        //Calling the ids for work type and Upload
        ArtistUploadType artistUploadType = artistUploadTypeService.getArtistUploadTypeById(artistUploadTypeId).orElseThrow(() -> new RuntimeException("uploadtype not found "));
        ArtistWorkType art = artistWorkTypeService.findWorkType(artistWorkTypeId).orElseThrow(() -> new RuntimeException("uploadtype not found "));
        
        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.startsWith("audio/") && !contentType.startsWith("video/"))) {
                throw new IllegalArgumentException("Only audio and video files are allowed");
            }
            
            String originalFileName = file.getOriginalFilename();
            String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                    + "_" + System.currentTimeMillis()
                    + originalFileName.substring(originalFileName.lastIndexOf('.'));
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            String fileUrl = "https://api.owellserver.ggff.net/api/music/view/" + fileName;
            music.setFileType(contentType);
            music.setFileUrl(fileUrl);
        }

        music.setRecordingCompanyTelephone(publisherTelephone);
        music.setLabelName(labelName);
        //music.setUploadedDate(uploadedDate);
        music.setDateRecorded(dateRecorded);
        music.setArranger(arranger);
        music.setPublishersName(publishersName);
        music.setArtist(artist);
        music.setAuthor(author);
        music.setAlbumName(albumName);
        music.setArtistId(ArtistId);
        music.setFeaturedArtist(featuredArtist);
        music.setProducer(producer);
        music.setCountry(country);
        music.setDuration(Duration);
        music.setComposer(composer);
        music.setPublisher(publisher);
        music.setPublisherAdress(publisherAdress);
        music.setPublisherTelephone(publisherTelephone);
        music.setRecordedBy(recordedBy);
        music.setAddressOfRecordingCompany(AddressOfRecordingCompany);
        music.setGroupOrBandOrStageName(GroupOrBandOrStageName);
        music.setTitle(title);
        music.setArtistUploadType(artistUploadType);
        music.setArtistWorkType(art);
        music.setUploadedDate(LocalDate.now());

        return artistWorkRepository.save(music);
    }
    
    public void deleteMusic(Long id) {
        artistWorkRepository.deleteById(id);
    }

    //Id Generation Methods
    //customes Ids generating
    //String contentId = generateContentId(title, ArtistId, uploadedDate.getYear());
    private String generateContentId(String tittle, String ArtistId) {
        String prefix = "NAM";
        String lastNamePart = tittle.length() >= 2 ? tittle.substring(0, 2).toUpperCase() : tittle.toUpperCase();
        String lastNamePart2 = ArtistId.length() >= 2 ? ArtistId.substring(0, 2).toUpperCase() : ArtistId.toUpperCase();
        //String yearPart = String.valueOf(uploadedDate).substring(2);
        //method

        // Get the current count of members from the database and add 1
        Long count = artistWorkRepository.count() + 1;

        // Format the count with leading zeros (e.g., 001, 002, ..., 1000)
        String counterPart = String.format("%01d", count);


        return prefix + lastNamePart  + lastNamePart2 +  counterPart;
    }

    //get Music by ID
    public List<ArtistWork> getMusicByUserId(Long userId) {
        return artistWorkRepository.findByUserId(userId);
    }

    //get All music
    public List<ArtistWork> ListOfMusic() {
        return artistWorkRepository.findAll();
    }


}
