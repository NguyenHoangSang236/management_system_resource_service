package com.management_system.resource.infrastucture.controller;

import com.management_system.resource.entities.request_dto.filter_requests.IngredientFilterRequest;
import com.management_system.resource.entities.request_dto.ingredient.AddIngredientRequest;
import com.management_system.resource.entities.request_dto.ingredient.EditIngredientRequest;
import com.management_system.resource.usecases.ingredient.*;
import com.management_system.utilities.constant.ConstantValue;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.core.validator.file.ImageFileValid;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Validated
@Tag(name = "Ingredient", description = "Operations related to managing ingredients")
@RestController
@RequestMapping(value = "/authen/ingredient", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class IngredientController {
    final AddNewIngredientsUseCase addNewIngredientsUseCase;
    final FilterIngredientsUseCase filterIngredientsUseCase;
    final EditIngredientUseCase editIngredientUseCase;
    final ViewIngredientDetailsByIdUseCase viewIngredientDetailsByIdUseCase;
    final DeleteIngredientUseCase deleteIngredientUseCase;
    final UseCaseExecutor useCaseExecutor;

    @Operation(summary = "Add new ingredient")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PostMapping(path = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewIngredient(
            @Valid
            @RequestPart("data")
            @NotNull
            AddIngredientRequest ingredientRequestBody,
            @Parameter(description = "File to upload", required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @NotNull
            @ImageFileValid
            @RequestPart("image")
            MultipartFile image
    ) {
        return useCaseExecutor.execute(
                addNewIngredientsUseCase,
                new AddNewIngredientsUseCase.InputValue(ingredientRequestBody, image),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Edit a selected ingredient")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PatchMapping(path = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<ApiResponse>> editIngredient(
            @Valid
            @RequestPart("data")
            @NotNull
            EditIngredientRequest ingredientRequest,
            @Parameter(description = "File to upload", required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @ImageFileValid(nullable = true)
            @RequestPart("image")
            MultipartFile image
    ) {
        return useCaseExecutor.execute(
                editIngredientUseCase,
                new EditIngredientUseCase.InputValue(ingredientRequest, image),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Get ingredients by filters")
    @PostMapping("/filter")
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

    @Operation(summary = "Delete ingredient by ID")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @DeleteMapping("/delete/{id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> deleteIngredientById(@PathVariable("id") String id) {
        return useCaseExecutor.execute(
                deleteIngredientUseCase,
                new DeleteIngredientUseCase.InputValue(id),
                ResponseMapper::map
        );
    }
}
