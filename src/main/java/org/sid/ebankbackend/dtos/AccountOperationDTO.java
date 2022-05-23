package org.sid.ebankbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankbackend.entities.BankAccount;
import org.sid.ebankbackend.enums.OpType;

import javax.persistence.*;
import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date date;
    private String description;
    private double amount;
    private OpType type;
}
