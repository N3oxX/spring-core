package com.template.spring.common.crud;

import com.template.spring.core.application.exception.UnknownEntityException;
import com.template.spring.core.web.dto.input.PaginatedDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface CrudService<I, D> {

    D create(D dto);

    D getById(I id) throws UnknownEntityException;

    D update(I id, D entity) throws UnknownEntityException;

    List<D> getAll();

    void delete(I id);

    D patch(I id, Map<String, Object> updates) throws UnknownEntityException, IllegalArgumentException;

    Page<D> getPaginated(PaginatedDto<D> paginatedDto) throws IllegalAccessException;

}
