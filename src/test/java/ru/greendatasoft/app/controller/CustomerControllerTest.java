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
import ru.greendatasoft.app.domain.dto.CustomerDto;
import ru.greendatasoft.app.domain.entity.Customer;
import ru.greendatasoft.app.domain.entity.LegalForm;
import ru.greendatasoft.app.mapper.CustomerMapper;
import ru.greendatasoft.app.util.DBHelper;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class CustomerControllerTest {

    private final DBHelper dbHelper;
    private final WebApplicationContext webApplicationContext;
    private final ObjectMapper objectMapper;
    private final CustomerMapper customerMapper;

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
    void createCustomer() throws Exception {
        CustomerDto dto = new CustomerDto()
                .setAddress("addr")
                .setName("name")
                .setForm(LegalForm.INDIVIDUAL)
                .setShortName("short");

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto))
        );

        CustomerDto savedDto = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), CustomerDto.class);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.address", equalTo(dto.getAddress())))
                .andExpect(jsonPath("$.name", equalTo(dto.getName())))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.shortName", equalTo(dto.getShortName())))
                .andExpect(jsonPath("$.form", equalTo(dto.getForm().name())));

        assertNotNull(savedDto);
        assertNotNull(savedDto.getVersion());
        assertNotNull(savedDto.getId());

        Optional<Customer> optionalBank = dbHelper.getCustomer(savedDto.getId());
        assertTrue(optionalBank.isPresent());
        Customer customer = optionalBank.get();
        assertEquals(dto.getAddress(), customer.getAddress());
        assertEquals(dto.getName(), customer.getName());
        assertEquals(dto.getShortName(), customer.getShortName());
        assertEquals(dto.getForm(), customer.getForm());
    }

    @Test
    void updateBank() throws Exception {
        Customer customer = dbHelper.getAnyCustomer().get();

        CustomerDto dto = customerMapper.toDto(customer);
        dto.setAddress("new addr");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/customer/{id}", dto.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(customer.getId().intValue())))
                .andExpect(jsonPath("$.address", equalTo(dto.getAddress())));
    }

    @Test
    void getBank() throws Exception {
        Customer customer = dbHelper.getAnyCustomer().get();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/{id}", customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(customer.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(customer.getName())));
    }

    @Test
    void deleteBank() throws Exception {
        Customer customer = dbHelper.getAnyCustomer().get();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/customer/{id}", customer.getId()));

        if (dbHelper.getDepositByCustomerId(customer.getId()).isEmpty()) {
            result.andExpect(status().isOk());
            assertFalse(dbHelper.getCustomer(customer.getId()).isPresent());
        } else {
            result.andExpect(status().is5xxServerError());
            assertTrue(dbHelper.getCustomer(customer.getId()).isPresent());
        }

    }

    @Test
    void filter() throws Exception {
        Customer customer = dbHelper.getAnyCustomer().get();

        FilterRequest filterRequest = new FilterRequest();
        filterRequest.setPage(0);
        filterRequest.setSize(10);

        Predicate p = new Predicate();
        p.setConnectionType(ConnectionType.AND);
        p.setCondition(new Condition()
                .setKey("id")
                .setLogicalCondition(LogicalConditionType.EQ)
                .setValue(customer.getId()));

        filterRequest.setPredicate(p);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer/filter")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(filterRequest))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id",
                        equalTo(customer.getId().intValue())))
                .andExpect(jsonPath("$.content[0].name", equalTo(customer.getName())));
    }
}