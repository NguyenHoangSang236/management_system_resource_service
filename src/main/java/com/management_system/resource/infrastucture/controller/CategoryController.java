package com.management_system.resource.infrastucture.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.entities.database.ingredient.Category;
import com.management_system.resource.entities.request_dto.CategoryRequest;
import com.management_system.resource.usecases.category.AddNewCategoriesUseCase;
import com.management_system.resource.usecases.category.DeleteCategoriesUseCase;
import com.management_system.resource.usecases.category.EditCategoryUseCase;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
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
    final DeleteCategoriesUseCase deleteCategoriesUseCase;
    final EditCategoryUseCase editCategoryUseCase;
    final AddNewCategoriesUseCase addNewCategoriesUseCase;
    final UseCaseExecutor useCaseExecutor;

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/addNewCategories")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewCategories(@RequestBody List<Category> categories, HttpServletRequest request) {
        return useCaseExecutor.execute(
                addNewCategoriesUseCase,
                new AddNewCategoriesUseCase.InputValue(request, categories),
                ResponseMapper::map
        );
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/editCategory")
    public CompletableFuture<ResponseEntity<ApiResponse>> editCategory(@RequestBody CategoryRequest categoryRequest, HttpServletRequest request) {
        return useCaseExecutor.execute(
                editCategoryUseCase,
                new EditCategoryUseCase.InputValue(categoryRequest),
                ResponseMapper::map
        );
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/deleteCategories")
    public CompletableFuture<ResponseEntity<ApiResponse>> deleteCategories(@RequestBody String json, HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> idList = objectMapper.readValue(json, new TypeReference<>() {
        });

        return useCaseExecutor.execute(
                deleteCategoriesUseCase,
                new DeleteCategoriesUseCase.InputValue(request, idList),
                ResponseMapper::map
        );
    }
}
