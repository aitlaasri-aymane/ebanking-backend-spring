package org.sid.ebankbackend.dtos;

import lombok.Data;
import org.sid.ebankbackend.enums.AccountStatus;

import java.util.Date;

@Data
public class CurrentBankAccountDTO extends BankAccountDTO { // should not be abstract class
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private String currency;
    private CustomerDTO customerDTO;
    private double overDraft;
}
