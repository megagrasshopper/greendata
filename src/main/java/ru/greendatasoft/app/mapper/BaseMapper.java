package ru.greendatasoft.app.mapper;

import java.util.List;

public interface BaseMapper<ENTITY, DTO> {

    DTO toDto(ENTITY entity);

    ENTITY fromDto(DTO dto);

    List<DTO> listToDto(List<ENTITY> entities);
}
