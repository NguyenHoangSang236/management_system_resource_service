package com.management_system.resource.infrastucture.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.entities.database.ingredient.Category;
import com.management_system.resource.entities.request_dto.CategoryRequest;
import com.management_system.resource.usecases.category.AddNewCategoriesUseCase;
import com.management_system.resource.usecases.category.DeleteCategoriesUseCase;
import com.management_system.resource.usecases.category.EditCategoryUseCase;
import com.management_system.utilities.constant.ConstantValue;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Category", description = "Operations related to managing categories of ingredients and menu")
@RestController
@RequestMapping(value = "/authen/category", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class CategoryController {
    final DeleteCategoriesUseCase deleteCategoriesUseCase;
    final EditCategoryUseCase editCategoryUseCase;
    final AddNewCategoriesUseCase addNewCategoriesUseCase;
    final UseCaseExecutor useCaseExecutor;

    @Operation(summary = "Add one or multiple categories")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PostMapping("/addNewCategories")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewCategories(
            @RequestBody
            List<Category> categories,
            HttpServletRequest request
    ) {
        return useCaseExecutor.execute(
                addNewCategoriesUseCase,
                new AddNewCategoriesUseCase.InputValue(request, categories),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Edit a selected category")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PatchMapping("/editCategory")
    public CompletableFuture<ResponseEntity<ApiResponse>> editCategory(
            @RequestBody
            CategoryRequest categoryRequest
    ) {
        return useCaseExecutor.execute(
                editCategoryUseCase,
                new EditCategoryUseCase.InputValue(categoryRequest),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Delete one or multiple categories by IDs")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PostMapping("/deleteCategories")
    public CompletableFuture<ResponseEntity<ApiResponse>> deleteCategories(
            @RequestBody List<String> idList,
            HttpServletRequest request
    ) {
        return useCaseExecutor.execute(
                deleteCategoriesUseCase,
                new DeleteCategoriesUseCase.InputValue(request, idList),
                ResponseMapper::map
        );
    }
}
