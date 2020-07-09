package ru.greendatasoft.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.greendatasoft.app.domain.condition.FilterRequest;
import ru.greendatasoft.app.domain.condition.PageRequestBuilder;
import ru.greendatasoft.app.domain.condition.SpecificationBuilder;
import ru.greendatasoft.app.domain.dto.DepositDto;
import ru.greendatasoft.app.domain.entity.Deposit;
import ru.greendatasoft.app.domain.repository.BankRepository;
import ru.greendatasoft.app.domain.repository.CustomerRepository;
import ru.greendatasoft.app.domain.repository.DepositRepository;
import ru.greendatasoft.app.mapper.DepositMapper;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements BaseCrudService<DepositDto> {

    private final DepositRepository depositRepository;
    private final CustomerRepository customerRepository;
    private final BankRepository bankRepository;
    private final DepositMapper depositMapper;

    @Override
    public DepositDto getById(Long id) {
        return depositMapper.toDto(depositRepository.findById(id).orElse(null));
    }

    @Override
    public DepositDto saveOrUpdate(DepositDto depositDto) {

        Deposit deposit = depositMapper.fromDto(depositDto);

        deposit.setBank(bankRepository.findById(depositDto.getBankId())
                .orElseThrow(() -> new IllegalArgumentException("Bank " + depositDto.getBankId() + " is not found"))
        );
        deposit.setCustomer(customerRepository.findById(depositDto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer " + depositDto.getCustomerId() + " is not found"))
        );

        return depositMapper.toDto(depositRepository.save(deposit));
    }

    @Override
    public Long delete(Long id) {
        depositRepository.deleteById(id);
        return id;
    }

    @Override
    public Page<DepositDto> filter(FilterRequest filter) {
        return depositRepository.findAll(
                SpecificationBuilder.getSpecification(filter.getPredicate()),
                PageRequestBuilder.getPageRequest(filter)
        ).map(depositMapper::toDto);
    }
}