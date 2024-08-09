package com.management_system.resource.infrastucture.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.management_system.resource.entities.database.ingredient.Ingredient;
import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.resource.entities.request_dto.IngredientFilterOptions;
import com.management_system.resource.entities.request_dto.SupplierFilterOptions;
import com.management_system.resource.usecases.ingredient.FilterIngredientsUseCase;
import com.management_system.resource.usecases.supplier.AddNewSuppliersUseCase;
import com.management_system.resource.usecases.supplier.FilterSuppliersUseCase;
import com.management_system.utilities.core.deserializer.FilterOptionsDeserializer;
import com.management_system.utilities.core.filter.FilterOption;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.request.FilterRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewSuppliers(@RequestBody String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Supplier> suppliers = objectMapper.readValue(json, new TypeReference<List<Supplier>>() {
        });

        return useCaseExecutor.execute(
                addNewSuppliersUseCase,
                new AddNewSuppliersUseCase.InputValue(suppliers),
                ResponseMapper::map
        );
    }
}
