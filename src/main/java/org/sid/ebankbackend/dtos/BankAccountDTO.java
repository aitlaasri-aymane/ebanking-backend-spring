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
public class BankAccountDTO { // abstract class if using table_per_class since we dont need to create a table for bankaccount just for current and saving accounts
    private String type;
}
