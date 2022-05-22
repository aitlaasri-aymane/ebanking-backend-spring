package org.sid.ebankbackend.services;

import org.sid.ebankbackend.dtos.CustomerDTO;
import org.sid.ebankbackend.entities.*;
import org.sid.ebankbackend.enums.OpType;
import org.sid.ebankbackend.exceptions.BalanceNotSufficentException;
import org.sid.ebankbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface IEBankService {
    Customer saveCustomer(Customer customer);
    CustomerDTO getCustomerbyID(Long id) throws CustomerNotFoundException;
    CurrentAccount saveCurrentAccount(Long customerID, double balance, double overdraft) throws CustomerNotFoundException;
    SavingAccount saveSavingAccount(Long customerID, double balance, double interestRate);
    List<CustomerDTO> listCustomers();
    List<BankAccount> listBankAccounts();
    BankAccount getBankAccountByID(String ID) throws BankAccountNotFoundException;
    void showBankAccDetail(String BankAccountID) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, double amount, String accountIdDestination) throws BankAccountNotFoundException, BalanceNotSufficentException;
}
