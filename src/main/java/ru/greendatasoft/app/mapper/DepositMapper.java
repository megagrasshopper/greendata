package ru.greendatasoft.app.mapper;


import org.mapstruct.Mapper;
import ru.greendatasoft.app.domain.dto.DepositDto;
import ru.greendatasoft.app.domain.entity.Deposit;

@Mapper(componentModel = "spring")
public interface DepositMapper extends BaseMapper<Deposit, DepositDto> {
}