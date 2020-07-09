package ru.greendatasoft.app.mapper;


import org.mapstruct.Mapper;
import ru.greendatasoft.app.domain.dto.BankDto;
import ru.greendatasoft.app.domain.entity.Bank;

@Mapper(componentModel = "spring")
public interface BankMapper extends BaseMapper<Bank, BankDto> {
}