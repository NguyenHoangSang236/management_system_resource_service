package com.management_system.ingredient.infrastucture.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.ingredient.entities.database.Category;
import com.management_system.ingredient.entities.database.Ingredient;
import com.management_system.ingredient.usecases.category.AddNewCategoriesUseCase;
import com.management_system.ingredient.usecases.ingredient.AddNewIngredientsUseCase;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.ResponseMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
@RequestMapping(value = "/authen/category", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class CategoryController {
    final AddNewCategoriesUseCase addNewCategoriesUseCase;
    final UseCaseExecutor useCaseExecutor;

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/addNewCategories")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewCategories(@RequestBody String json, HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Category> categories = objectMapper.readValue(json, new TypeReference<List<Category>>(){});

        return useCaseExecutor.execute(
                addNewCategoriesUseCase,
                new AddNewCategoriesUseCase.InputValue(request, categories),
                ResponseMapper::map
        );
    }
}
