package com.template.spring.crud;



import org.springframework.web.bind.annotation.*;



public abstract class CrudController<T, ID, DTO, DBO> {

    private final CrudService<T, ID, DTO, DBO> service;

    protected CrudController(CrudService<T, ID, DTO, DBO> service) {
        this.service = service;
    }

    @PostMapping
    public DTO create(@RequestBody DTO entity) {
        return service.create(entity);
    }


}
