package ru.greendatasoft.app.service;

import org.springframework.data.domain.Page;
import ru.greendatasoft.app.domain.condition.FilterRequest;

public interface BaseCrudService<T> {

    T getById(Long id);

    T saveOrUpdate(T dto);

    Long delete(Long id);

    Page<T> filter(FilterRequest filter);
}