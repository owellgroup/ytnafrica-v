package com.example.musicroyalties.controllers.lookupControllers;

import com.example.musicroyalties.models.BankName;
import com.example.musicroyalties.services.lookupservices.BankNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/bankname")
public class BankNameController {
    @Autowired
    private BankNameService bankNameService;
    
    //post
    @PostMapping("/post")
    public BankName createBankName(@RequestBody BankName bankName) {
        return bankNameService.saveBankName(bankName);
    }
    
    //get all
    @GetMapping("/all")
    public List<BankName> getAllBankName() {
        return bankNameService.findAllBankName();
    }
    //Get by Id
    @GetMapping("/{id}")
    public Optional<BankName> findBankNameById(@PathVariable Long id) {
        return bankNameService.findBankNameById(id);
    }

    //Delete by Id
    @DeleteMapping("/delete/{id}")
    public void deleteBankNameById(@PathVariable Long id) {
        bankNameService.deleteBankNameById(id);
    }
    //update
    @PutMapping("/update/{id}")
    public BankName updateBankName(@PathVariable Long id, @RequestBody BankName bankName) {
        bankName.setId(id);
        return bankNameService.updateBankName(bankName);
    }

}
