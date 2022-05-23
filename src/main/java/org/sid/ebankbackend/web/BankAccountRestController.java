package org.sid.ebankbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankbackend.dtos.*;
import org.sid.ebankbackend.entities.BankAccount;
import org.sid.ebankbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankbackend.services.IEBankService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class BankAccountRestController {
    private IEBankService ieBankService;

    @GetMapping("/bankaccounts")
    public List<BankAccountDTO> bankAccountList(){
        return ieBankService.listBankAccounts();
    }

    @GetMapping("/bankaccounts/{id}")
    public BankAccountDTO findBankAccountByID(@PathVariable(name = "id") String id) throws BankAccountNotFoundException {
        return ieBankService.getBankAccountByID(id);
    }

    @DeleteMapping("/bankaccounts/delete/{id}")
    public void deleteBankAccountDTO(@PathVariable(name = "id") String id){
        ieBankService.deleteBankAccount(id);
    }

    @GetMapping("/bankaccounts/{id}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable(name = "id") String id){
        return ieBankService.accountHistory(id);
    }

    @GetMapping("/bankaccounts/{id}/operationsPage")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) throws BankAccountNotFoundException {
        return ieBankService.getAccountHistory(id,page,size);
    }
}
