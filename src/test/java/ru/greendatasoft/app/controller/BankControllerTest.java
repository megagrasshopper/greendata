package ru.greendatasoft.app.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.greendatasoft.app.domain.condition.Condition;
import ru.greendatasoft.app.domain.condition.ConnectionType;
import ru.greendatasoft.app.domain.condition.FilterRequest;
import ru.greendatasoft.app.domain.condition.LogicalConditionType;
import ru.greendatasoft.app.domain.condition.Predicate;
import ru.greendatasoft.app.domain.dto.BankDto;
import ru.greendatasoft.app.domain.entity.Bank;
import ru.greendatasoft.app.mapper.BankMapper;
import ru.greendatasoft.app.util.DBHelper;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BankControllerTest {

    private final DBHelper dbHelper;
    private final WebApplicationContext webApplicationContext;
    private final ObjectMapper objectMapper;
    private final BankMapper bankMapper;

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
    void createBank() throws Exception {
        BankDto dto = new BankDto()
                .setBik("bik")
                .setName("name");

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/bank")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto))
        );

        BankDto savedDto = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), BankDto.class);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.bik", equalTo(dto.getBik())))
                .andExpect(jsonPath("$.name", equalTo(dto.getName())))
                .andExpect(jsonPath("$.id", notNullValue()));

        assertNotNull(savedDto);
        assertNotNull(savedDto.getVersion());
        assertNotNull(savedDto.getId());

        Optional<Bank> optionalBank = dbHelper.getBank(savedDto.getId());
        assertTrue(optionalBank.isPresent());
        Bank bank = optionalBank.get();
        assertEquals(dto.getBik(), bank.getBik());
        assertEquals(dto.getName(), bank.getName());
    }

    @Test
    void updateBank() throws Exception {
        Bank bank = dbHelper.getAnyBank().get();

        BankDto dto = bankMapper.toDto(bank);
        dto.setBik("new bik");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/bank/{id}", dto.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(bank.getId().intValue())))
                .andExpect(jsonPath("$.bik", equalTo(dto.getBik())));
    }

    @Test
    void getBank() throws Exception {
        Bank bank = dbHelper.getAnyBank().get();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bank/{id}", bank.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(bank.getId().intValue())))
                .andExpect(jsonPath("$.bik", equalTo(bank.getBik())))
                .andExpect(jsonPath("$.name",
                        equalTo(bank.getName())));
    }

    @Test
    void deleteBank() throws Exception {
        Bank bank = dbHelper.getAnyBank().get();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/bank/{id}", bank.getId()));

        if (dbHelper.getDepositByBankId(bank.getId()).isEmpty()) {
            result.andExpect(status().isOk());
            assertFalse(dbHelper.getBank(bank.getId()).isPresent());
        } else {
            result.andExpect(status().is5xxServerError());
            assertTrue(dbHelper.getBank(bank.getId()).isPresent());
        }

    }

    @Test
    void filter() throws Exception {
        Bank bank = dbHelper.getAnyBank().get();

        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setPage(0);
        filterRequest.setSize(10);

        Predicate p = new Predicate();
        p.setConnectionType(ConnectionType.AND);
        p.setCondition(new Condition()
                .setKey("id")
                .setLogicalCondition(LogicalConditionType.EQ)
                .setValue(bank.getId()));

        filterRequest.setPredicate(p);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/bank/filter")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(filterRequest))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id",
                        equalTo(bank.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name", equalTo(bank.getName())))
                .andExpect(jsonPath("$.content[0].bik", equalTo(bank.getBik())));
    }
}