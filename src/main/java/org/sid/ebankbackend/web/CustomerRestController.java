package org.sid.ebankbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankbackend.dtos.CustomerDTO;
import org.sid.ebankbackend.entities.Customer;
import org.sid.ebankbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankbackend.services.IEBankService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private IEBankService ieBankService;

    @GetMapping("/customers")
    public List<CustomerDTO> customerList(){
        return ieBankService.listCustomers();
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO findCustomerbyID(@PathVariable(name = "id") Long id) throws CustomerNotFoundException {
        return ieBankService.getCustomerbyID(id);
    }
}
