package org.sid.ebankbackend.services;

import lombok.AllArgsConstructor;
import org.sid.ebankbackend.dtos.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

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
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerID){
        customerRepository.deleteById(customerID);
    }

    @Override
    public void deleteBankAccount(String bankAccountID){
        bankAccountRepository.deleteById(bankAccountID);
    }


    @Override
    public CustomerDTO getCustomerbyID(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new CustomerNotFoundException("Customer not found!"));
        CustomerDTO customerDTO = bankAccountMapper.fromCustomer(customer);
        return customerDTO;
    }

    @Override
    public List<CustomerDTO> searchForCustumers(String keyword) {
        List<Customer> customers = customerRepository.searchForCustomer(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> bankAccountMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public CurrentBankAccountDTO saveCurrentAccount(Long bankAccountID, double balance, double overdraft) throws CustomerNotFoundException {
        CurrentAccount currentAccount = new CurrentAccount();
        Customer customer = customerRepository.findById(bankAccountID).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found!");
        currentAccount.setCurrency("MAD");
        currentAccount.setBalance(Math.random()*90000);
        currentAccount.setCreatedAt(new Date());
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overdraft);
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setId(UUID.randomUUID().toString());
        CurrentAccount savedCurrentAccount = bankAccountRepository.save(currentAccount);
        return bankAccountMapper.fromCurrentAccount(savedCurrentAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingAccount(Long bankAccountID, double balance, double interestRate) throws CustomerNotFoundException {
        SavingAccount savingAccount = new SavingAccount();
        Customer customer = customerRepository.findById(bankAccountID).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found!");
        savingAccount.setCurrency("MAD");
        savingAccount.setBalance(Math.random()*90000);
        savingAccount.setCreatedAt(new Date());
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setId(UUID.randomUUID().toString());
        SavingAccount savedSavingAccount = bankAccountRepository.save(savingAccount);
        return bankAccountMapper.fromSavingAccount(savedSavingAccount);
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
    public List<BankAccountDTO> listBankAccounts() {
        List<BankAccount> bankAccountList = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOList = bankAccountList.stream()
                .map(bankAccount -> {
                    if (bankAccount instanceof SavingAccount) {
                        return bankAccountMapper.fromSavingAccount((SavingAccount) bankAccount);
                    }
                    else {
                        return bankAccountMapper.fromCurrentAccount((CurrentAccount) bankAccount);
                    }
                })
                .collect(Collectors.toList());
        return bankAccountDTOList;
    }

    @Override
    public List<BankAccountDTO> getBankAccountsByCustomerId(Long customerId) {
        List<BankAccount> bankAccounts = bankAccountRepository.findByCustomerId(customerId);
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
                    if (bankAccount instanceof SavingAccount) {
                        SavingAccount savingAccount = (SavingAccount) bankAccount;
                        return bankAccountMapper.fromSavingAccount(savingAccount);
                    } else {
                        CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                        return bankAccountMapper.fromCurrentAccount(currentAccount);
                    }
                }
        ).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public BankAccountDTO getBankAccountByID(String ID) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(ID).orElseThrow(()-> new BankAccountNotFoundException("Bank Acc not found!"));
        if (bankAccount instanceof CurrentAccount) {
            return bankAccountMapper.fromCurrentAccount((CurrentAccount) bankAccount);
        } else {
            return bankAccountMapper.fromSavingAccount((SavingAccount) bankAccount);
        }
    }

    @Override
    public void showBankAccDetail(String BankAccountID) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(BankAccountID).orElseThrow(()-> new BankAccountNotFoundException("Bank Acc not found!"));
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
    public void debit(String BankAccountID, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount = bankAccountRepository.findById(BankAccountID).orElseThrow(()-> new BankAccountNotFoundException("Bank Acc not found!"));
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
    public void credit(String BankAccountID, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(BankAccountID).orElseThrow(()-> new BankAccountNotFoundException("Bank Acc not found!"));
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

    @Override
    public List<AccountOperationDTO> accountHistory(String accountID) {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountID);
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.stream().map(accountOperation -> bankAccountMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
        return accountOperationDTOS;
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String id, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(()->new BankAccountNotFoundException("Bank Acc not found!"));
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByIdDesc(id, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.stream().map(accountOperation -> bankAccountMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

}
