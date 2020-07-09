package ru.greendatasoft.app.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.greendatasoft.app.domain.entity.Bank;
import ru.greendatasoft.app.domain.entity.Customer;
import ru.greendatasoft.app.domain.entity.Deposit;
import ru.greendatasoft.app.domain.entity.LegalForm;
import ru.greendatasoft.app.domain.repository.BankRepository;
import ru.greendatasoft.app.domain.repository.CustomerRepository;
import ru.greendatasoft.app.domain.repository.DepositRepository;

@Component
@RequiredArgsConstructor
@Transactional
public class DBHelper {

    private final BankRepository bankRepository;
    private final CustomerRepository customerRepository;
    private final DepositRepository depositRepository;

    public void cleanDb() {
        depositRepository.deleteAll();
        bankRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<Deposit> getDepositByBankId(Long id) {
        return depositRepository.findAll().stream()
                .filter(d -> d.getBank().getId().equals(id))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Deposit> getDepositByCustomerId(Long id) {
        return depositRepository.findAll().stream()
                .filter(d -> d.getCustomer().getId().equals(id))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Optional<Deposit> getDeposit(Long id) {
        return depositRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Bank> getBank(Long id) {
        return bankRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> getCustomer(Long id) {
        return customerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Deposit> getAnyDeposit() {
        return depositRepository.findAll().stream().findAny();
    }

    @Transactional(readOnly = true)
    public Optional<Bank> getAnyBank() {
        return bankRepository.findAll().stream().findAny();
    }

    @Transactional(readOnly = true)
    public Optional<Customer> getAnyCustomer() {
        return customerRepository.findAll().stream().findAny();
    }

    public void generateTestData() {
        Bank bank1 = new Bank();
        bank1.setBik("bik1");
        bank1.setName("bank 1");
        bank1 = bankRepository.save(bank1);

        Bank bank2 = new Bank();
        bank2.setBik("bik2");
        bank2.setName("bank 2");
        bank2 = bankRepository.save(bank2);

        Bank bank3 = new Bank();
        bank3.setBik("bik3");
        bank3.setName("bank 3");
        bank3 = bankRepository.save(bank3);

        Customer cust1 = new Customer();
        cust1.setForm(LegalForm.INDIVIDUAL);
        cust1.setAddress("addr 1");
        cust1.setName("cust 1");
        cust1.setShortName("cust_1");
        cust1 = customerRepository.save(cust1);

        Customer cust2 = new Customer();
        cust2.setForm(LegalForm.INDIVIDUAL);
        cust2.setAddress("addr 2");
        cust2.setName("cust 2");
        cust2.setShortName("cust_2");
        cust2 = customerRepository.save(cust2);

        Customer cust3 = new Customer();
        cust3.setForm(LegalForm.INDIVIDUAL);
        cust3.setAddress("addr 3");
        cust3.setName("cust 3");
        cust3.setShortName("cust_3");
        cust3 = customerRepository.save(cust3);


        Deposit d1 = new Deposit();
        d1.setCustomer(cust1);
        d1.setBank(bank1);
        d1.setMonths(1);
        d1.setOpenDate(LocalDate.now());
        d1.setPercent(BigDecimal.TEN);


        Deposit d2 = new Deposit();
        d2.setCustomer(cust1);
        d2.setBank(bank2);
        d2.setMonths(1);
        d2.setOpenDate(LocalDate.now());
        d2.setPercent(BigDecimal.TEN);


        Deposit d3 = new Deposit();
        d3.setCustomer(cust2);
        d3.setBank(bank3);
        d3.setMonths(1);
        d3.setOpenDate(LocalDate.now());
        d3.setPercent(BigDecimal.TEN);

        Deposit d4 = new Deposit();
        d4.setCustomer(cust3);
        d4.setBank(bank3);
        d4.setMonths(1);
        d4.setOpenDate(LocalDate.now());
        d4.setPercent(BigDecimal.valueOf(3));

        depositRepository.saveAll(Arrays.asList(d1, d2, d3, d4));

    }

}