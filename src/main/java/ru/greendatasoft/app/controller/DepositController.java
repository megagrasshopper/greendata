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
import ru.greendatasoft.app.domain.dto.DepositDto;
import ru.greendatasoft.app.service.BaseCrudService;

@RestController
@RequiredArgsConstructor
@Api("Deposit operations")
@RequestMapping("/api/deposit")
public class DepositController {

    private final BaseCrudService<DepositDto> depositService;

    @ApiOperation("Create deposit")
    @PostMapping("")
    public DepositDto createDeposit(@Valid @RequestBody DepositDto dto) {
        return depositService.saveOrUpdate(dto);
    }

    @ApiOperation("Update deposit")
    @PutMapping("{id}")
    public DepositDto updateCustomer(@PathVariable("id") Long id, @Valid @RequestBody DepositDto dto) {
        if (!id.equals(dto.getId())) {
            throw new IllegalArgumentException("Wrong id " + id);
        }
        return depositService.saveOrUpdate(dto);
    }

    @ApiOperation("Get deposit by id")
    @GetMapping("/{id}")
    public DepositDto getDeposit(@PathVariable("id") Long id) {
        return depositService.getById(id);
    }

    @ApiOperation("Delete deposit by id")
    @DeleteMapping("/{id}")
    public Long deleteDeposit(@PathVariable("id") Long id) {
        return depositService.delete(id);
    }

    @ApiOperation("Filter deposits")
    @PostMapping("/filter")
    public Page<DepositDto> filter(@Valid @RequestBody FilterRequest filterRequest) {
        return depositService.filter(filterRequest);
    }
}