package com.example.musicroyalties.services;

import com.example.musicroyalties.models.MemberDetails;
import com.example.musicroyalties.models.Status;
import com.example.musicroyalties.models.User;
import com.example.musicroyalties.repositories.MemberDetailsRepository;
import com.example.musicroyalties.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberDetailsService {
    
    @Autowired
    private MemberDetailsRepository memberDetailsRepository;
    
    @Autowired
    private StatusRepository statusRepository;
    
    @Autowired
    private EmailService emailService;
    
    public MemberDetails createMemberDetails(MemberDetails memberDetails, User user) {

        //customs ID Generation
        String userId = generateUserId(memberDetails.getSurname(), memberDetails.getFirstName(), memberDetails.getBirthDate().getYear());
        memberDetails.setArtistId(userId);

        // Set default status to PENDING
        Status pendingStatus = statusRepository.findByStatus(Status.EStatus.PENDING)
                .orElseGet(() -> {
                    Status status = new Status();
                    status.setStatus(Status.EStatus.PENDING);
                    return statusRepository.save(status);
                });
        
        memberDetails.setUser(user);
        memberDetails.setStatus(pendingStatus);
        memberDetails.setEmail(user.getEmail()); // Set email from user
        
        return memberDetailsRepository.save(memberDetails);
    }
    
    public MemberDetails getMemberDetailsByUser(User user) {
        return memberDetailsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Member details not found"));
    }
    
    public List<MemberDetails> getPendingMemberDetails() {
        Status pendingStatus = statusRepository.findByStatus(Status.EStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("Pending status not found"));
        return memberDetailsRepository.findByStatusId(pendingStatus.getId());
    }
    
    public MemberDetails approveMemberDetails(Long memberId, String ipiNumber) {
        MemberDetails memberDetails = memberDetailsRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member details not found"));
        
        Status approvedStatus = statusRepository.findByStatus(Status.EStatus.APPROVED)
                .orElseGet(() -> {
                    Status status = new Status();
                    status.setStatus(Status.EStatus.APPROVED);
                    return statusRepository.save(status);
                });
        
        memberDetails.setStatus(approvedStatus);
        memberDetails.setIPI_number(ipiNumber);
        
        MemberDetails savedMember = memberDetailsRepository.save(memberDetails);
        
        // Send approval email using the email from memberDetails
        emailService.sendProfileApprovalEmail(memberDetails.getEmail());
        
        return savedMember;
    }
    
    public MemberDetails rejectMemberDetails(Long memberId, String notes) {
        MemberDetails memberDetails = memberDetailsRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member details not found"));
        
        Status rejectedStatus = statusRepository.findByStatus(Status.EStatus.REJECTED)
                .orElseGet(() -> {
                    Status status = new Status();
                    status.setStatus(Status.EStatus.REJECTED);
                    return statusRepository.save(status);
                });
        
        memberDetails.setStatus(rejectedStatus);
        memberDetails.setNotes(notes);
        
        MemberDetails savedMember = memberDetailsRepository.save(memberDetails);
        
        // Send rejection email using the email from memberDetails
        emailService.sendProfileRejectionEmail(memberDetails.getEmail(), notes);
        
        return savedMember;
    }
    
    public Optional<MemberDetails> getMemberDetailsById(Long id) {
        return memberDetailsRepository.findById(id);
    }
    //find Members details by user ID
    public Optional<MemberDetails> getByUserId(Long userId) {
        return memberDetailsRepository.findByUserId(userId);
    }
    
    public List<MemberDetails> getAllMemberDetails() {
        return memberDetailsRepository.findAll();
    }
    
    public MemberDetails updateMemberDetails(Long id, MemberDetails memberDetails) {
        MemberDetails existingMember = memberDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member details not found"));
        
        // Update fields
        existingMember.setFirstName(memberDetails.getFirstName());
        existingMember.setSurname(memberDetails.getSurname());
        existingMember.setPseudonym(memberDetails.getPseudonym());
        existingMember.setPhoneNumber(memberDetails.getPhoneNumber());
        existingMember.setGroupNameORStageName(memberDetails.getGroupNameORStageName());
        existingMember.setMaritalStatus(memberDetails.getMaritalStatus());
        existingMember.setMemberCategory(memberDetails.getMemberCategory());
        existingMember.setNoOFDependents(memberDetails.getNoOFDependents());
        existingMember.setTypeOfWork(memberDetails.getTypeOfWork());
        existingMember.setGender(memberDetails.getGender());
        existingMember.setLine1(memberDetails.getLine1());
        existingMember.setLine2(memberDetails.getLine2());
        existingMember.setCity(memberDetails.getCity());
        existingMember.setRegion(memberDetails.getRegion());
        existingMember.setPoBox(memberDetails.getPoBox());
        existingMember.setPostalCode(memberDetails.getPostalCode());
        existingMember.setCountry(memberDetails.getCountry());
        existingMember.setBirthDate(memberDetails.getBirthDate());
        existingMember.setPlaceOfBirth(memberDetails.getPlaceOfBirth());
        existingMember.setIdOrPassportNumber(memberDetails.getIdOrPassportNumber());
        existingMember.setNationality(memberDetails.getNationality());
        existingMember.setOccupation(memberDetails.getOccupation());
        existingMember.setNameOfEmployer(memberDetails.getNameOfEmployer());
        existingMember.setAddressOfEmployer(memberDetails.getAddressOfEmployer());
        existingMember.setNameOfTheBand(memberDetails.getNameOfTheBand());
        existingMember.setDateFounded(memberDetails.getDateFounded());
        existingMember.setNumberOfBand(memberDetails.getNumberOfBand());
        existingMember.setBankName(memberDetails.getBankName());
        existingMember.setAccountHolderName(memberDetails.getAccountHolderName());
        existingMember.setBankAccountNumber(memberDetails.getBankAccountNumber());
        existingMember.setBankAccountType(memberDetails.getBankAccountType());
        existingMember.setBankBranchName(memberDetails.getBankBranchName());
        existingMember.setBankBranchNumber(memberDetails.getBankBranchNumber());
        existingMember.setTittle(memberDetails.getTittle());
        existingMember.setEmail(memberDetails.getEmail());
        existingMember.setIPI_number(memberDetails.getIPI_number());
        existingMember.setIdNumber(memberDetails.getIdNumber());
        existingMember.setNotes(memberDetails.getNotes());

        
        return memberDetailsRepository.save(existingMember);
    }

    //Delete Method
    public void deleteMemberDetailsById(Long id) {
         memberDetailsRepository.deleteById(id);
    }

    //Custom Ids Generations
    private String generateUserId(String Surname, String FirstName, int birthYear) {
        String prefix = "YTN";
        String lastNamePart = Surname.length() >= 2 ? Surname.substring(0, 2).toUpperCase() : Surname.toUpperCase();
        String lastNamePart2 = FirstName.length() >= 2 ? FirstName.substring(0, 2).toUpperCase() : FirstName.toUpperCase();
        String yearPart = String.valueOf(birthYear).substring(2);
        //method

        // Get the current count of members from the database and add 1
        Long count = memberDetailsRepository.count() + 1;

        // Format the count with leading zeros (e.g., 001, 002, ..., 1000)
        String counterPart = String.format("%01d", count);


        return prefix + lastNamePart + yearPart + lastNamePart2 +  counterPart;
    }
    //WHAT

}
