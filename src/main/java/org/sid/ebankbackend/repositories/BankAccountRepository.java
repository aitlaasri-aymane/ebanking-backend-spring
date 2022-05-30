package org.sid.ebankbackend.repositories;

import org.sid.ebankbackend.entities.BankAccount;
import org.sid.ebankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
    List<BankAccount> findByCustomerId(Long id);
}
