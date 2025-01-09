package com.management_system.resource.infrastucture.controller;

import com.management_system.resource.entities.request_dto.MenuRequest;
import com.management_system.resource.usecases.menu.AddNewMenuProductsUseCase;
import com.management_system.resource.usecases.menu.ViewMenuProductByIdUseCase;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Add one or multiple menu items")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PostMapping("/authen/menu/addNewProducts")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewProducts(
            @RequestBody
            @Valid
            List<@Valid MenuRequest> request,
            HttpServletRequest servletRequest
    ) {
        return useCaseExecutor.execute(
                addNewMenuProductsUseCase,
                new AddNewMenuProductsUseCase.InputValue(request, servletRequest),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Get menu item's details by ID")
    @GetMapping("/unauthen/menu/menuId={id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> viewMenuItemById(@PathVariable("id") String menuId) {
        return useCaseExecutor.execute(
                viewMenuProductByIdUseCase,
                new ViewMenuProductByIdUseCase.InputValue(menuId),
                ResponseMapper::map
        );
    }
}
