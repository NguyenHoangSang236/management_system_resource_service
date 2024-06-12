package com.management_system.ingredient.infrastucture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.management_system.ingredient.entities.database.Ingredient;
import com.management_system.ingredient.entities.request_dto.IngredientFilterOptions;
import com.management_system.ingredient.entities.request_dto.IngredientRequest;
import com.management_system.ingredient.usecases.ingredient.AddNewIngredientUseCase;
import com.management_system.ingredient.usecases.ingredient.EditIngredientUseCase;
import com.management_system.ingredient.usecases.ingredient.FilterIngredientsUseCase;
import com.management_system.utilities.core.deserializer.FilterOptionsDeserializer;
import com.management_system.utilities.core.filter.FilterOption;
import com.management_system.utilities.core.usecase.UseCase;
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
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/authen/ingredient", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class IngredientController {
    final AddNewIngredientUseCase addNewIngredientUseCase;
    final FilterIngredientsUseCase filterIngredientsUseCase;
    final EditIngredientUseCase editIngredientUseCase;
    final UseCaseExecutor useCaseExecutor;

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/addNewIngredient")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewIngredient(@RequestBody String json, HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Ingredient ingredient = objectMapper.readValue(json, Ingredient.class);

        return useCaseExecutor.execute(
                addNewIngredientUseCase,
                new AddNewIngredientUseCase.InputValue(request, ingredient),
                ResponseMapper::map
        );
    }


    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/editIngredient")
    public CompletableFuture<ResponseEntity<ApiResponse>> editIngredient(@RequestParam String id, @RequestBody String json, HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        IngredientRequest ingredientReq = objectMapper.readValue(json, IngredientRequest.class);

        return useCaseExecutor.execute(
                editIngredientUseCase,
                new EditIngredientUseCase.InputValue(request, id, ingredientReq),
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
}
