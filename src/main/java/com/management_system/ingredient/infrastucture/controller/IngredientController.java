package com.management_system.ingredient.infrastucture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.ingredient.entities.database.Ingredient;
import com.management_system.ingredient.usecases.ingredient.AddNewIngredientUseCase;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.ResponseMapper;
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
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/authen/ingredient", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class IngredientController {
    final AddNewIngredientUseCase addNewIngredientUseCase;
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
}
