package org.sid.ebankbackend.services;

import org.sid.ebankbackend.dtos.*;
import org.sid.ebankbackend.entities.*;
import org.sid.ebankbackend.enums.OpType;
import org.sid.ebankbackend.exceptions.BalanceNotSufficentException;
import org.sid.ebankbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface IEBankService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerID);
    void deleteBankAccount(String bankAccountID);
    CustomerDTO getCustomerbyID(Long id) throws CustomerNotFoundException;
    List<CustomerDTO> searchForCustumers(String keyword);
    CurrentBankAccountDTO saveCurrentAccount(Long bankAccountID, double balance, double overdraft) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingAccount(Long bankAccountID, double balance, double interestRate) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    List<BankAccountDTO> listBankAccounts();
    List<BankAccountDTO> getBankAccountsByCustomerId(Long customerId);
    BankAccountDTO getBankAccountByID(String ID) throws BankAccountNotFoundException;
    void showBankAccDetail(String BankAccountID) throws BankAccountNotFoundException;
    void debit(String BankAccountID, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String BankAccountID, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, double amount, String accountIdDestination) throws BankAccountNotFoundException, BalanceNotSufficentException;
    List<AccountOperationDTO> accountHistory(String accountID);
    AccountHistoryDTO getAccountHistory(String id, int page, int size) throws BankAccountNotFoundException;
}
