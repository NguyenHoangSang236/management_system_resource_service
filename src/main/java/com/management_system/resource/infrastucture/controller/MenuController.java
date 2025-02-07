package com.management_system.resource.infrastucture.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.entities.request_dto.menu.AddMenuRequest;
import com.management_system.resource.usecases.menu.AddNewMenuProductsUseCase;
import com.management_system.resource.usecases.menu.ViewMenuProductByIdUseCase;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Menu", description = "Operations related to managing menu items")
@Validated
@RestController
@RequestMapping(consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class MenuController {
    final UseCaseExecutor useCaseExecutor;
    final AddNewMenuProductsUseCase addNewMenuProductsUseCase;
    final ViewMenuProductByIdUseCase viewMenuProductByIdUseCase;

    @Operation(summary = "Add new menu item")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PostMapping("/authen/menu/add")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewProduct(
            @Valid
            @RequestPart("data")
            @NotNull
            AddMenuRequest requestBody,
            @Parameter(description = "File to upload", required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @NotNull
            @ImageFileValid
            @RequestPart("image")
            MultipartFile image,
            HttpServletRequest servletRequest
    ) {
        return useCaseExecutor.execute(
                addNewMenuProductsUseCase,
                new AddNewMenuProductsUseCase.InputValue(requestBody, image, servletRequest),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Get menu item's details by ID")
    @GetMapping("/unauthen/menu/{id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> viewMenuItemById(@PathVariable("id") String menuId) {
        return useCaseExecutor.execute(
                viewMenuProductByIdUseCase,
                new ViewMenuProductByIdUseCase.InputValue(menuId),
                ResponseMapper::map
        );
    }
}
