package org.sid.ebankbackend.repositories;

import org.sid.ebankbackend.dtos.BankAccountDTO;
import org.sid.ebankbackend.entities.AccountOperation;
import org.sid.ebankbackend.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
public List<AccountOperation> findByBankAccountId(String accountID);
public Page<AccountOperation> findByBankAccountIdOrderByIdDesc(String accountID, Pageable pageable);
}
