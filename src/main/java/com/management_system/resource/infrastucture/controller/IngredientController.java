package com.management_system.resource.infrastucture.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.management_system.resource.entities.database.ingredient.Ingredient;
import com.management_system.resource.entities.request_dto.IngredientFilterOptions;
import com.management_system.resource.entities.request_dto.IngredientRequest;
import com.management_system.resource.usecases.ingredient.EditIngredientUseCase;
import com.management_system.resource.usecases.ingredient.AddNewIngredientsUseCase;
import com.management_system.resource.usecases.ingredient.FilterIngredientsUseCase;
import com.management_system.resource.usecases.ingredient.ViewIngredientDetailsByIdUseCase;
import com.management_system.utilities.core.deserializer.FilterOptionsDeserializer;
import com.management_system.utilities.core.filter.FilterOption;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.FilterRequest;
import com.management_system.utilities.entities.ResponseMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/authen/ingredient", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class IngredientController {
    final AddNewIngredientsUseCase addNewIngredientsUseCase;
    final FilterIngredientsUseCase filterIngredientsUseCase;
    final EditIngredientUseCase editIngredientUseCase;
    final ViewIngredientDetailsByIdUseCase viewIngredientDetailsByIdUseCase;
    final UseCaseExecutor useCaseExecutor;

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/addNewIngredients")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewIngredient(@RequestBody String json, HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Ingredient> ingredients = objectMapper.readValue(json, new TypeReference<List<Ingredient>>() {
        });

        return useCaseExecutor.execute(
                addNewIngredientsUseCase,
                new AddNewIngredientsUseCase.InputValue(request, ingredients),
                ResponseMapper::map
        );
    }


    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/editIngredient")
    public CompletableFuture<ResponseEntity<ApiResponse>> editIngredient(@RequestBody String json, HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        IngredientRequest ingredientReq = objectMapper.readValue(json, IngredientRequest.class);

        return useCaseExecutor.execute(
                editIngredientUseCase,
                new EditIngredientUseCase.InputValue(request, ingredientReq),
                ResponseMapper::map
        );
    }


    @PostMapping("/filterIngredient")
    public CompletableFuture<ResponseEntity<ApiResponse>> filterIngredient(@RequestBody String json, HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        module.addDeserializer(FilterOption.class, new FilterOptionsDeserializer(IngredientFilterOptions.class));
        objectMapper.registerModule(module);

        FilterRequest filterRequest = objectMapper.readValue(json, FilterRequest.class);

        return useCaseExecutor.execute(
                filterIngredientsUseCase,
                new FilterIngredientsUseCase.InputValue(filterRequest),
                ResponseMapper::map
        );
    }


    @GetMapping("/ingredientId={id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> viewIngredientById(@PathVariable("id") String id) {
        return useCaseExecutor.execute(
                viewIngredientDetailsByIdUseCase,
                new ViewIngredientDetailsByIdUseCase.InputValue(id),
                ResponseMapper::map
        );
    }
}
