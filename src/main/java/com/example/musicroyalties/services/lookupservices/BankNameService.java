package com.example.musicroyalties.services.lookupservices;

import com.example.musicroyalties.models.BankName;
import com.example.musicroyalties.repositories.BankNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankNameService {
    @Autowired
    private BankNameRepository bankNameRepository;

    //Saving
    public BankName saveBankName(BankName bankName) {
        return bankNameRepository.save(bankName);
    }

    //Get All Banks
    public List<BankName> findAllBankName() {
        return bankNameRepository.findAll();
    }
    //get by Id
    public Optional<BankName> findBankNameById(Long id) {
        return bankNameRepository.findById(id);
    }

    //delete
    public void deleteBankNameById(Long id) {
        bankNameRepository.deleteById(id);
    }
    //Update
    public BankName updateBankName(BankName bankName) {
        BankName bankName1 = bankNameRepository.findById(bankName.getId()).orElse(null);
        bankName1.setBankName(bankName.getBankName());
        return bankNameRepository.save(bankName1);

    }
}
