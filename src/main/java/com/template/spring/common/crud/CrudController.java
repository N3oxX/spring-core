package com.template.spring.common.crud;

import com.template.spring.core.application.exception.UnknownEntityException;
import com.template.spring.core.web.dto.input.PaginatedDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Abstract CRUD Controller to handle basic CRUD operations.
 *
 * @param <T> Entity type
 * @param <I> ID type
 * @param <D> DTO type
 * @param <B> DBO type
 * @param <R> Response type
 */
public abstract class CrudController<T, I, D, B, R> {

    private final CrudService<I, D> service;
    private final CrudMapper<T, D, B, R> mapper;

    protected CrudController(CrudService<I, D> service, CrudMapper<T, D, B, R> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Create a new resource", description = "Creates a new resource using the provided DTO.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Resource created successfully",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    public ResponseEntity<R> create(@Valid @RequestBody D dto) {
        D createdDto = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.DTOToResponse(createdDto));
    }

    @Operation(summary = "Get a resource by ID", description = "Fetches a resource based on its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resource fetched successfully",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<R> get(@PathVariable I id) throws UnknownEntityException {
        D dto = service.getById(id);
        return ResponseEntity.ok(mapper.DTOToResponse(dto));
    }

    @Operation(summary = "Update a resource by ID", description = "Updates a resource with the provided data.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resource updated successfully",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<R> update(@PathVariable I id, @Valid @RequestBody D dto) throws UnknownEntityException {
        D updatedDto = service.update(id, dto);
        return ResponseEntity.ok(mapper.DTOToResponse(updatedDto));
    }

    @Operation(summary = "Fetch all resources", description = "Fetches all available resources.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resources fetched successfully",
                    content = @Content(schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<List<R>> findAll() {
        List<D> listDto = service.getAll();
        return ResponseEntity.ok(listDto.stream().map(mapper::DTOToResponse).collect(Collectors.toList()));
    }

    @Operation(summary = "Delete a resource by ID", description = "Deletes the resource with the specified ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Resource deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable I id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Patch a resource by ID", description = "Applies partial updates to the resource.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resource patched successfully",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400", description = "Invalid updates"),
            @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<R> patch(
            @PathVariable I id,
            @RequestBody Map<String, Object> updates) throws UnknownEntityException, IllegalArgumentException {
        D updatedDto = service.patch(id, updates);
        return ResponseEntity.ok(mapper.DTOToResponse(updatedDto));
    }

    @Operation(summary = "Get paginated resources", description = "Fetches a paginated list of resources.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paginated resources fetched successfully",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination request")
    })
    @PostMapping("/paginated")
    public Page<R> getPaginated(@RequestBody PaginatedDTO<D> paginatedDto) throws IllegalAccessException {
        return service.getPaginated(paginatedDto).map(mapper::DTOToResponse);
    }

}
