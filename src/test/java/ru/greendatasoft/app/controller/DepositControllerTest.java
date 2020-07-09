package ru.greendatasoft.app.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.greendatasoft.app.domain.condition.Condition;
import ru.greendatasoft.app.domain.condition.ConnectionType;
import ru.greendatasoft.app.domain.condition.FilterRequest;
import ru.greendatasoft.app.domain.condition.LogicalConditionType;
import ru.greendatasoft.app.domain.condition.Predicate;
import ru.greendatasoft.app.domain.condition.SortDirection;
import ru.greendatasoft.app.domain.dto.BankDto;
import ru.greendatasoft.app.domain.dto.CustomerDto;
import ru.greendatasoft.app.domain.dto.DepositDto;
import ru.greendatasoft.app.domain.entity.Bank;
import ru.greendatasoft.app.domain.entity.Customer;
import ru.greendatasoft.app.domain.entity.Deposit;
import ru.greendatasoft.app.mapper.DepositMapper;
import ru.greendatasoft.app.util.DBHelper;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class DepositControllerTest {

    private final DBHelper dbHelper;
    private final WebApplicationContext webApplicationContext;
    private final ObjectMapper objectMapper;
    private final DepositMapper depositMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        setUpDb();
    }

    void setUpDb() {
        dbHelper.cleanDb();
        dbHelper.generateTestData();
    }

    @Test
    void createDeposit() throws Exception {

        Bank bank = dbHelper.getAnyBank().get();
        Customer customer = dbHelper.getAnyCustomer().get();

        LocalDate now = LocalDate.now();

        DepositDto dto = new DepositDto()
                .setBank(new BankDto().setId(bank.getId()))
                .setCustomer(new CustomerDto().setId(customer.getId()))
                .setMonths(10)
                .setOpenDate(now)
                .setPercent(BigDecimal.valueOf(1.5));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/deposit")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.bank.id", equalTo(bank.getId().intValue())))
                .andExpect(jsonPath("$.customer.id", equalTo(customer.getId().intValue())))
                .andExpect(jsonPath("$.months", equalTo(10)))
                .andExpect(jsonPath("$.percent", equalTo(1.5)))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("Create deposit absent customer")
    void createDeposit2() throws Exception {
        Bank bank = dbHelper.getAnyBank().get();
        DepositDto dto = new DepositDto()
                .setBank(new BankDto().setId(bank.getId()))
                .setCustomer(new CustomerDto().setId(100000L))
                .setMonths(10)
                .setOpenDate(LocalDate.now())
                .setPercent(BigDecimal.valueOf(1.5));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/deposit")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("Create deposit absent bank")
    void createDeposit3() throws Exception {
        Customer customer = dbHelper.getAnyCustomer().get();
        DepositDto dto = new DepositDto()
                .setBank(new BankDto().setId(-1L))
                .setCustomer(new CustomerDto().setId(customer.getId()))
                .setMonths(10)
                .setOpenDate(LocalDate.now())
                .setPercent(BigDecimal.valueOf(1.5));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/deposit")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().is4xxClientError());

    }

    @Test
    void updateDeposit() throws Exception {
        Deposit deposit = dbHelper.getAnyDeposit().get();

        Integer months = 55;

        DepositDto dto = depositMapper.toDto(deposit);
        dto.setMonths(months);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/deposit/{id}", dto.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(deposit.getId().intValue())))
                .andExpect(jsonPath("$.months", equalTo(months)));
    }

    @DisplayName("Update deposit wrong id")
    @Test
    void updateDeposit2() throws Exception {
        Deposit deposit = dbHelper.getAnyDeposit().get();

        DepositDto dto = depositMapper.toDto(deposit);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/deposit/{id}", 555)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void getDeposit() throws Exception {

        Deposit deposit = dbHelper.getAnyDeposit().get();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/deposit/{id}", deposit.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(deposit.getId().intValue())))
                .andExpect(jsonPath("$.months", equalTo(deposit.getMonths())))
                .andExpect(jsonPath("$.bank.id",
                        equalTo(deposit.getBank().getId().intValue())))
                .andExpect(jsonPath("$.customer.id",
                        equalTo(deposit.getCustomer().getId().intValue())));
    }

    @Test
    void deleteDeposit() throws Exception {
        Deposit deposit = dbHelper.getAnyDeposit().get();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/deposit/{id}", deposit.getId())
        ).andExpect(status().isOk());

        assertFalse(dbHelper.getDeposit(deposit.getId()).isPresent());
    }

    @Test
    void filter() throws Exception {

        Deposit deposit = dbHelper.getAnyDeposit().get();

        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setPage(0);
        filterRequest.setSize(10);

        LinkedHashMap<String, SortDirection> sort = new LinkedHashMap<>();
        sort.put("months", SortDirection.ASC);
        sort.put("id", SortDirection.DESC);

        filterRequest.setSort(sort);

        Predicate p = new Predicate();
        p.setConnectionType(ConnectionType.AND);
        p.setCondition(new Condition()
                .setKey("id")
                .setLogicalCondition(LogicalConditionType.EQ)
                .setValue(deposit.getId()));

        Predicate p2 = new Predicate();
        p2.setCondition(new Condition()
                .setKey("bank.name")
                .setLogicalCondition(LogicalConditionType.LIKE)
                .setValue(deposit.getBank().getName() + "%"));

        p.setPredicates(Collections.singletonList(p2));

        filterRequest.setPredicate(p);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/deposit/filter")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(filterRequest))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].bank.id",
                        equalTo(deposit.getBank().getId().intValue())))
                .andExpect(jsonPath("$.content[0].customer.id",
                        equalTo(deposit.getCustomer().getId().intValue())))
                .andExpect(jsonPath("$.content[0].months", equalTo(deposit.getMonths())))
                .andExpect(jsonPath("$.content[0].id", equalTo(deposit.getId().intValue())));
    }
}