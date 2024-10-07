package com.management_system.resource.infrastucture.controller;

import com.management_system.resource.entities.request_dto.MenuRequest;
import com.management_system.resource.usecases.menu.AddNewProductsUseCase;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Validated
@RestController
@RequestMapping(consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class MenuController {
    final UseCaseExecutor useCaseExecutor;
    final AddNewProductsUseCase addNewProductsUseCase;

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/authen/menu/addNewProducts")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewProducts(@RequestBody @Valid List<@Valid MenuRequest> request, HttpServletRequest servletRequest) {
        return useCaseExecutor.execute(
                addNewProductsUseCase,
                new AddNewProductsUseCase.InputValue(request, servletRequest),
                ResponseMapper::map
        );
    }
}
