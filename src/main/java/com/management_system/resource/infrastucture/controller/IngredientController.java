package com.management_system.resource.infrastucture.controller;

import com.management_system.resource.entities.request_dto.filter_requests.IngredientFilterRequest;
import com.management_system.resource.entities.request_dto.IngredientRequest;
import com.management_system.resource.usecases.ingredient.AddNewIngredientsUseCase;
import com.management_system.resource.usecases.ingredient.EditIngredientUseCase;
import com.management_system.resource.usecases.ingredient.FilterIngredientsUseCase;
import com.management_system.resource.usecases.ingredient.ViewIngredientDetailsByIdUseCase;
import com.management_system.utilities.constant.ConstantValue;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Ingredient", description = "Operations related to managing ingredients")
@RestController
@RequestMapping(value = "/authen/ingredient", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class IngredientController {
    final AddNewIngredientsUseCase addNewIngredientsUseCase;
    final FilterIngredientsUseCase filterIngredientsUseCase;
    final EditIngredientUseCase editIngredientUseCase;
    final ViewIngredientDetailsByIdUseCase viewIngredientDetailsByIdUseCase;
    final UseCaseExecutor useCaseExecutor;

    @Operation(summary = "Add one or multiple ingredients")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PostMapping("/addNewIngredients")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewIngredient(
            @Valid
            @RequestBody
            List<@Valid IngredientRequest> ingredientRequests,
            HttpServletRequest request
    ) {
        return useCaseExecutor.execute(
                addNewIngredientsUseCase,
                new AddNewIngredientsUseCase.InputValue(request, ingredientRequests),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Edit a selected ingredient")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PatchMapping("/editIngredient")
    public CompletableFuture<ResponseEntity<ApiResponse>> editIngredient(
            @RequestBody IngredientRequest ingredientReq,
            HttpServletRequest request
    ) {
        return useCaseExecutor.execute(
                editIngredientUseCase,
                new EditIngredientUseCase.InputValue(request, ingredientReq),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Get ingredients by filters")
    @PostMapping("/filterIngredients")
    public CompletableFuture<ResponseEntity<ApiResponse>> filterIngredient(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Filter for ingredient")
            @RequestBody IngredientFilterRequest filterRequest
    ) {
        return useCaseExecutor.execute(
                filterIngredientsUseCase,
                new FilterIngredientsUseCase.InputValue(filterRequest),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Get ingredient's details by ID")
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> viewIngredientById(@PathVariable("id") String id) {
        return useCaseExecutor.execute(
                viewIngredientDetailsByIdUseCase,
                new ViewIngredientDetailsByIdUseCase.InputValue(id),
                ResponseMapper::map
        );
    }
}
