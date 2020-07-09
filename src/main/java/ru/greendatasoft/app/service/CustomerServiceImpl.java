package ru.greendatasoft.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greendatasoft.app.domain.condition.FilterRequest;
import ru.greendatasoft.app.domain.condition.PageRequestBuilder;
import ru.greendatasoft.app.domain.condition.SpecificationBuilder;
import ru.greendatasoft.app.domain.dto.CustomerDto;
import ru.greendatasoft.app.domain.repository.CustomerRepository;
import ru.greendatasoft.app.mapper.CustomerMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements BaseCrudService<CustomerDto> {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    @Override
    public CustomerDto getById(Long id) {
        return customerMapper.toDto(customerRepository.findById(id).orElse(null));
    }

    @Override
    public CustomerDto saveOrUpdate(CustomerDto customerDto) {
        return customerMapper.toDto(customerRepository.save(customerMapper.fromDto(customerDto)));
    }

    @Override
    public Long delete(Long id) {
        customerRepository.deleteById(id);
        return id;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CustomerDto> filter(FilterRequest filterRequest) {
        return customerRepository.findAll(
                SpecificationBuilder.getSpecification(filterRequest.getPredicate()),
                PageRequestBuilder.getPageRequest(filterRequest)
        ).map(customerMapper::toDto);
    }
}
