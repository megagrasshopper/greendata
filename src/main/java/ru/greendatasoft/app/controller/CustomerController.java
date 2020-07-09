package ru.greendatasoft.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greendatasoft.app.domain.condition.FilterRequest;
import ru.greendatasoft.app.domain.dto.CustomerDto;
import ru.greendatasoft.app.service.BaseCrudService;

@RestController
@RequiredArgsConstructor
@Api("Customer operations")
@RequestMapping("/api/customer")
public class CustomerController {

    private final BaseCrudService<CustomerDto> customerService;

    @ApiOperation("Create customer")
    @PostMapping("")
    public CustomerDto createCustomer(@Valid @RequestBody CustomerDto dto) {
        return customerService.saveOrUpdate(dto);
    }

    @ApiOperation("Update customer")
    @PutMapping("{id}")
    public CustomerDto updateCustomer(@PathVariable("id") Long id, @Valid @RequestBody CustomerDto dto) {
        if (!id.equals(dto.getId())) {
            throw new IllegalArgumentException("Wrong id " + id);
        }
        return customerService.saveOrUpdate(dto);
    }

    @ApiOperation("Get customer by id")
    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable("id") Long id) {
        return customerService.getById(id);
    }

    @ApiOperation("Delete customer by id")
    @DeleteMapping("/{id}")
    public Long deleteCustomer(@PathVariable("id") Long id) {
        return customerService.delete(id);
    }

    @ApiOperation("Filter customers")
    @PostMapping("/filter")
    public Page<CustomerDto> filter(@Valid @RequestBody FilterRequest filterRequest) {
        return customerService.filter(filterRequest);
    }

}
