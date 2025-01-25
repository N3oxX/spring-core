package com.template.spring.common.crud;


import com.template.spring.core.application.exception.UnknownEntityException;
import com.template.spring.core.web.dto.input.EmployeePaginatedDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrudServiceImpl<T, I, D, B, R> implements CrudService<I, D> {

    private final CrudRepositoryImpl<T, I, D, B, R> repository;
    private final CrudMapper<T, D, B, R> mapper;


    public CrudServiceImpl(CrudRepositoryImpl<T, I, D, B, R> repository, CrudMapper<T, D, B, R> mapper) {
        this.repository = repository;
        this.mapper = mapper;

    }


    @Override
    public D create(D dto) {
        T entity = mapper.DTOToEntity(dto);
        T savedEntity = repository.save(entity);
        return mapper.EntityToDTO(savedEntity);
    }

    @Override
    public D getById(I id) throws UnknownEntityException {
        return mapper.EntityToDTO(repository.getById(id));
    }

    @Override
    public D update(I id, D dto) throws UnknownEntityException {
        return mapper.EntityToDTO(repository.update(id, mapper.DTOToEntity(dto)));
    }

    @Override
    public List<D> getAll() {
        return repository.getAll().stream().map(mapper::EntityToDTO).collect(Collectors.toList());
    }

    @Override
    public void delete(I id) {
        repository.delete(id);
    }

    @Override
    public D patch(I id, Map<String, Object> updates) throws UnknownEntityException, IllegalArgumentException {
        return mapper.EntityToDTO(repository.patch(id, updates));
    }


    @Override
    public Page<D> getPaginated(EmployeePaginatedDto<D> paginatedDto) throws IllegalAccessException {
        Pageable pageable = PageRequest.of(paginatedDto.getCurrentPage(), paginatedDto.getPageSize(),
                Sort.by(Sort.Direction.fromString(paginatedDto.getOrder().getOrderType()),
                        paginatedDto.getOrder().getColumn()));
        Page<T> paginatedEntities = repository.findPaginated(mapper.DTOToEntity(paginatedDto.getSearchFields()), pageable);

        return paginatedEntities.map(mapper::EntityToDTO);
    }


}
