package com.management_system.resource.infrastucture.controller;

import com.management_system.resource.entities.request_dto.filter_requests.options.SupplierFilterOptions;
import com.management_system.resource.entities.request_dto.filter_requests.SupplierFilterRequest;
import com.management_system.resource.entities.request_dto.SupplierRequest;
import com.management_system.resource.usecases.supplier.AddNewSuppliersUseCase;
import com.management_system.resource.usecases.supplier.FilterSuppliersUseCase;
import com.management_system.resource.usecases.supplier.ViewSupplierDetailsByIdUseCase;
import com.management_system.utilities.constant.ConstantValue;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.request.FilterRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Supplier", description = "Operations related to managing suppliers")
@RestController
@RequestMapping(value = "/authen/supplier", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class SupplierController {
    final UseCaseExecutor useCaseExecutor;
    final FilterSuppliersUseCase filterSuppliersUseCase;
    final AddNewSuppliersUseCase addNewSuppliersUseCase;
    final ViewSupplierDetailsByIdUseCase viewSupplierDetailsByIdUseCase;


    @Operation(summary = "Get suppliers by filters")
    @PostMapping("/filterSuppliers")
    public CompletableFuture<ResponseEntity<ApiResponse>> filterSuppliers(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Filter for supplier")
            @RequestBody
            SupplierFilterRequest filterRequest
    ) {
        return useCaseExecutor.execute(
                filterSuppliersUseCase,
                new FilterSuppliersUseCase.InputValue(filterRequest),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Add one or multiple suppliers")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PostMapping("/addNewSuppliers")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewSuppliers(
            @Valid
            @RequestBody
            List<@Valid SupplierRequest> suppliers
    ) {
        return useCaseExecutor.execute(
                addNewSuppliersUseCase,
                new AddNewSuppliersUseCase.InputValue(suppliers),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Get supplier's details by ID")
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> viewSupplierById(@PathVariable("id") String id) {
        return useCaseExecutor.execute(
                viewSupplierDetailsByIdUseCase,
                new ViewSupplierDetailsByIdUseCase.InputValue(id),
                ResponseMapper::map
        );
    }
}
