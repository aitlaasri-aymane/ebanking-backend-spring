package org.sid.ebankbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankbackend.dtos.CustomerDTO;
import org.sid.ebankbackend.entities.Customer;
import org.sid.ebankbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankbackend.services.IEBankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // to verify API fast use /swagger-ui.html by using its dependancy springdoc openapi. Documentation in /v3/api-docs
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

    @PostMapping("/customers/add")
    public CustomerDTO saveCustomerDTO(@RequestBody CustomerDTO customerDTO){
        return ieBankService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/update/{id}")
    public CustomerDTO updateCustomerDTO(@PathVariable(name = "id") Long id,@RequestBody CustomerDTO customerDTO){
        customerDTO.setId(id);
        return ieBankService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/delete/{id}")
    public void deleteCustomerDTO(@PathVariable(name = "id") Long id){
        ieBankService.deleteCustomer(id);
    }
}
