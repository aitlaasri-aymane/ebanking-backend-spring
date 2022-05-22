package org.sid.ebankbackend.services;

import lombok.AllArgsConstructor;
import org.sid.ebankbackend.dtos.CustomerDTO;
import org.sid.ebankbackend.entities.*;
import org.sid.ebankbackend.enums.AccountStatus;
import org.sid.ebankbackend.enums.OpType;
import org.sid.ebankbackend.exceptions.BalanceNotSufficentException;
import org.sid.ebankbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankbackend.mappers.BankAccountMapperImpl;
import org.sid.ebankbackend.repositories.AccountOperationRepository;
import org.sid.ebankbackend.repositories.BankAccountRepository;
import org.sid.ebankbackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@AllArgsConstructor
public class EBankServiceImpl implements IEBankService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl bankAccountMapper;

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public CustomerDTO getCustomerbyID(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new CustomerNotFoundException("Customer not found!"));
        CustomerDTO customerDTO = bankAccountMapper.fromCustomer(customer);
        return customerDTO;
    }

    @Override
    public CurrentAccount saveCurrentAccount(Long customerID, double balance, double overdraft) throws CustomerNotFoundException {
        CurrentAccount currentAccount = new CurrentAccount();
        Customer customer = customerRepository.findById(customerID).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found!");
        currentAccount.setCurrency("MAD");
        currentAccount.setBalance(Math.random()*90000);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overdraft);
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setId(UUID.randomUUID().toString());
        return bankAccountRepository.save(currentAccount);
    }

    @Override
    public SavingAccount saveSavingAccount(Long customerID, double balance, double interestRate) {
        SavingAccount savingAccount = new SavingAccount();
        Customer customer = customerRepository.findById(customerID).orElse(null);
        if (customer == null)
            throw new RuntimeException("Customer not found!");
        savingAccount.setCurrency("MAD");
        savingAccount.setBalance(Math.random()*90000);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setId(UUID.randomUUID().toString());
        return bankAccountRepository.save(savingAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customerList = customerRepository.findAll();
        List<CustomerDTO> customerDTOList = customerList.stream()
                .map(customer -> bankAccountMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        /*
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        customerRepository.findAll().forEach(customer -> {
            customerDTOList.add(bankAccountMapper.fromCustomer(customer));
        });
         */
        return customerDTOList;
    }

    @Override
    public List<BankAccount> listBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccount getBankAccountByID(String ID) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(ID).orElseThrow(()-> new BankAccountNotFoundException("Bank Acc not found!"));
        return bankAccount;
    }

    @Override
    public void showBankAccDetail(String BankAccountID) throws BankAccountNotFoundException {
        BankAccount bankAccount = this.getBankAccountByID(BankAccountID);
        System.out.println("*******************");
        System.out.println("ID : "+bankAccount.getId());
        System.out.println("balance : "+bankAccount.getBalance());
        System.out.println("currency : "+bankAccount.getCurrency());
        System.out.println("status : "+bankAccount.getStatus());
        System.out.println("customer : "+bankAccount.getCustomer().getName());
        System.out.println("class : "+bankAccount.getClass().getName());
        if (bankAccount instanceof CurrentAccount)
            System.out.println("Over Draft : "+((CurrentAccount) bankAccount).getOverDraft());
        else if (bankAccount instanceof SavingAccount)
            System.out.println("Interest rate : "+((SavingAccount) bankAccount).getInterestRate());
        System.out.println("Operations :");
        bankAccount.getOperations().forEach(accountOperation -> {
            System.out.println("\t Op type : "+accountOperation.getType() + "\t Op date : "+accountOperation.getDate() + "\t Op amount : "+accountOperation.getAmount());
            System.out.println("--------------");
        });
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount = getBankAccountByID(accountId);
        if (bankAccount.getBalance()<amount){
            throw new BalanceNotSufficentException("Balance not sufficient!");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setType(OpType.DEBIT);
        accountOperation.setDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccountByID(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setType(OpType.CREDIT);
        accountOperation.setDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, double amount, String accountIdDestination) throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdSource,amount, "Transfer to "+ accountIdDestination);
        credit(accountIdDestination,amount, "Transfer from "+ accountIdSource);
    }

}
