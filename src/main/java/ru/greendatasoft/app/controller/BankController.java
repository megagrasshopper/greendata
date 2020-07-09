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
import ru.greendatasoft.app.domain.dto.BankDto;
import ru.greendatasoft.app.service.BaseCrudService;

@RestController
@RequiredArgsConstructor
@Api("Bank operations")
@RequestMapping("/api/bank")
public class BankController {

    private final BaseCrudService<BankDto> bankService;

    @ApiOperation("Create bank")
    @PostMapping("")
    public BankDto createBank(@Valid @RequestBody BankDto dto) {
        return bankService.saveOrUpdate(dto);
    }

    @ApiOperation("Update bank")
    @PutMapping("{id}")
    public BankDto updateBank(@PathVariable("id") Long id, @Valid @RequestBody BankDto dto) {
        if (!id.equals(dto.getId())) {
            throw new IllegalArgumentException("Wrong id " + id);
        }
        return bankService.saveOrUpdate(dto);
    }

    @ApiOperation("Get bank by id")
    @GetMapping("/{id}")
    public BankDto getBank(@PathVariable("id") Long id) {
        return bankService.getById(id);
    }

    @ApiOperation("Get bank by id")
    @DeleteMapping("/{id}")
    public Long deleteBank(@PathVariable("id") Long id) {
        return bankService.delete(id);
    }

    @ApiOperation("Filter banks")
    @PostMapping("/filter")
    public Page<BankDto> filter(@Valid @RequestBody FilterRequest filter) {
        return bankService.filter(filter);
    }

}
