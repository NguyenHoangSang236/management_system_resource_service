package com.management_system.resource.infrastucture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.management_system.resource.entities.request_dto.SupplierFilterOptions;
import com.management_system.resource.entities.request_dto.SupplierRequest;
import com.management_system.resource.usecases.supplier.AddNewSuppliersUseCase;
import com.management_system.resource.usecases.supplier.FilterSuppliersUseCase;
import com.management_system.resource.usecases.supplier.ViewSupplierDetailsByIdUseCase;
import com.management_system.utilities.core.deserializer.FilterOptionsDeserializer;
import com.management_system.utilities.core.filter.FilterOption;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.request.FilterRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/authen/supplier", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class SupplierController {
    final UseCaseExecutor useCaseExecutor;
    final FilterSuppliersUseCase filterSuppliersUseCase;
    final AddNewSuppliersUseCase addNewSuppliersUseCase;
    final ViewSupplierDetailsByIdUseCase viewSupplierDetailsByIdUseCase;


    @PostMapping("/filterSuppliers")
    public CompletableFuture<ResponseEntity<ApiResponse>> filterSuppliers(@RequestBody String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        module.addDeserializer(FilterOption.class, new FilterOptionsDeserializer(SupplierFilterOptions.class));
        objectMapper.registerModule(module);

        FilterRequest filterRequest = objectMapper.readValue(json, FilterRequest.class);

        return useCaseExecutor.execute(
                filterSuppliersUseCase,
                new FilterSuppliersUseCase.InputValue(filterRequest),
                ResponseMapper::map
        );
    }


    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/addNewSuppliers")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewSuppliers(@Valid @RequestBody List<@Valid SupplierRequest> suppliers) {
        return useCaseExecutor.execute(
                addNewSuppliersUseCase,
                new AddNewSuppliersUseCase.InputValue(suppliers),
                ResponseMapper::map
        );
    }

    @GetMapping("/supplierId={id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> viewSupplierById(@PathVariable("id") String id) {
        return useCaseExecutor.execute(
                viewSupplierDetailsByIdUseCase,
                new ViewSupplierDetailsByIdUseCase.InputValue(id),
                ResponseMapper::map
        );
    }
}
