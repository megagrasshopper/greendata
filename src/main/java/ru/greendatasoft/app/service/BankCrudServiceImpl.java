package ru.greendatasoft.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greendatasoft.app.domain.condition.FilterRequest;
import ru.greendatasoft.app.domain.condition.PageRequestBuilder;
import ru.greendatasoft.app.domain.condition.SpecificationBuilder;
import ru.greendatasoft.app.domain.dto.BankDto;
import ru.greendatasoft.app.domain.repository.BankRepository;
import ru.greendatasoft.app.mapper.BankMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class BankCrudServiceImpl implements BaseCrudService<BankDto> {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    @Transactional(readOnly = true)
    @Override
    public BankDto getById(Long id) {
        return bankMapper.toDto(bankRepository.findById(id).orElse(null));
    }

    @Override
    public BankDto saveOrUpdate(BankDto bankDto) {
        return bankMapper.toDto(bankRepository.save(bankMapper.fromDto(bankDto)));
    }

    @Override
    public Long delete(Long id) {
        bankRepository.deleteById(id);
        return id;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BankDto> filter(FilterRequest filter) {
        return bankRepository.findAll(
                SpecificationBuilder.getSpecification(filter.getPredicate()),
                PageRequestBuilder.getPageRequest(filter)
        ).map(bankMapper::toDto);
    }
}