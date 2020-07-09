package ru.greendatasoft.app.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.greendatasoft.app.domain.entity.LegalForm;

@Accessors(chain = true)
@Data
public class CustomerDto {
    private Long id;
    private String name;
    private String shortName;
    private String address;
    private LegalForm form;
    private Long version;
}