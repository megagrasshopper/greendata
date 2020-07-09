package ru.greendatasoft.app.domain.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class BankDto {
    private Long id;
    @NotNull
    private String name;
    private String bik;
    private Long version;
}
