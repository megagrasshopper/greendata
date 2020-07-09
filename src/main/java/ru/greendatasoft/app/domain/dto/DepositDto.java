package ru.greendatasoft.app.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class DepositDto {
    private Long id;
    @NotNull
    private BankDto bank;
    @NotNull
    private CustomerDto customer;
    private LocalDate openDate;
    @NotNull
    @Min(0)
    private BigDecimal percent;
    @NotNull
    @Min(1)
    private Integer months;
    private Long version;

    @JsonIgnore
    public Long getBankId() {
        return bank == null ? null : bank.getId();
    }

    @JsonIgnore
    public Long getCustomerId() {
        return customer == null ? null : customer.getId();
    }
}