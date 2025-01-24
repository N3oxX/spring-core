package com.template.spring.application.service;


import com.template.spring.application.exception.UnknownEntityException;
import com.template.spring.web.dto.input.EmployeePaginatedDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public abstract class CrudController<T, I, D, B, R> {

    private final CrudService<I, D> service;
    private final CrudMapper<T, D, B, R> mapper;

    protected CrudController(CrudService<I, D> service, CrudMapper<T, D, B, R> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<R> create(@Valid @RequestBody D dto) {
        D createdDto = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.DTOToResponse(createdDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<R> get(@PathVariable I id) throws UnknownEntityException {
        D dto = service.getById(id);
        return ResponseEntity.ok(mapper.DTOToResponse(dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<R> update(@PathVariable I id, @Valid @RequestBody D dto) throws UnknownEntityException {
        D updatedDto = service.update(id, dto);
        return ResponseEntity.ok(mapper.DTOToResponse(updatedDto));
    }


    @GetMapping("/all")
    public ResponseEntity<List<R>> findAll() {
        List<D> listDto = service.getAll();
        return ResponseEntity.ok(listDto.stream().map(mapper::DTOToResponse).collect(Collectors.toList()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable I id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<R> patch(
            @PathVariable I id,
            @RequestBody Map<String, Object> updates) throws UnknownEntityException, IllegalArgumentException {
        D updatedDto = service.patch(id, updates);
        return ResponseEntity.ok(mapper.DTOToResponse(updatedDto));
    }

    @PostMapping("/paginated")
    public Page<R> getPaginated(@RequestBody EmployeePaginatedDto<D> paginatedDto) throws IllegalAccessException {
        return service.getPaginated(paginatedDto).map(mapper::DTOToResponse);
    }


}
