package org.sid.ebankbackend;

import org.sid.ebankbackend.entities.BankAccount;
import org.sid.ebankbackend.entities.CurrentAccount;
import org.sid.ebankbackend.entities.Customer;
import org.sid.ebankbackend.entities.SavingAccount;
import org.sid.ebankbackend.enums.AccountStatus;
import org.sid.ebankbackend.exceptions.BalanceNotSufficentException;
import org.sid.ebankbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankbackend.services.EBankServiceImpl;
import org.sid.ebankbackend.services.IEBankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Currency;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }

    //@Bean
    CommandLineRunner showBankAcc(IEBankService ieBankService){
        return args -> {
            ieBankService.showBankAccDetail("36bd41d5-2a41-4c5c-b864-584e6cb7382b");
            ieBankService.showBankAccDetail("a206af0c-56e3-4ce0-8282-59013e284d1f");
        };
    }

    //@Bean
    CommandLineRunner start(IEBankService ieBankService){
        return args -> {
            Stream.of("Aymane","Imad","Khalid").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                ieBankService.saveCustomer(customer);
            });

            ieBankService.listCustomers().forEach(customer->{
                try {
                    ieBankService.saveSavingAccount(customer.getId(), 50000+Math.random() * 90000, 5.5);
                    ieBankService.saveCurrentAccount(customer.getId(), 50000+Math.random() * 120000, 9000);
                } catch (CustomerNotFoundException e){
                    e.printStackTrace();
                }
            });

            ieBankService.listBankAccounts().forEach(bankAccount -> {
                for (int i= 0; i<10; i++){
                    try {
                        ieBankService.credit(bankAccount.getId(),1000+Math.random()*120000, "Credit");
                        ieBankService.debit(bankAccount.getId(),1000+Math.random()*60000, "Debit");
                    } catch (BankAccountNotFoundException e)
                    {
                        e.printStackTrace();
                    } catch (BalanceNotSufficentException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        };
    }
}
