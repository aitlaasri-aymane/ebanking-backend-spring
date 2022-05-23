package org.sid.ebankbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankbackend.entities.AccountOperation;
import org.sid.ebankbackend.entities.Customer;
import org.sid.ebankbackend.enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
public class SavingBankAccountDTO extends BankAccountDTO { // should not be abstract class
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private String currency;
    private CustomerDTO customerDTO;
    private double interestRate;
}
