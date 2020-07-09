package ru.greendatasoft.app.mapper;

import org.mapstruct.Mapper;
import ru.greendatasoft.app.domain.dto.CustomerDto;
import ru.greendatasoft.app.domain.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends BaseMapper<Customer, CustomerDto> {
}