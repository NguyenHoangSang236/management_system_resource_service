package com.management_system.resource.infrastucture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.management_system.resource.entities.database.facility.Facility;
import com.management_system.resource.entities.request_dto.filter_requests.FacilityFilterRequest;
import com.management_system.resource.entities.request_dto.filter_requests.options.FacilityFilterOptions;
import com.management_system.resource.entities.request_dto.FacilityRequest;
import com.management_system.resource.usecases.facility.AddNewFacilityUseCase;
import com.management_system.resource.usecases.facility.EditFacilityUseCase;
import com.management_system.resource.usecases.facility.FilterFacilitiesUseCase;
import com.management_system.resource.usecases.facility.ViewFacilityDetailsByIdUseCase;
import com.management_system.utilities.constant.ConstantValue;
import com.management_system.utilities.core.deserializer.FilterOptionsDeserializer;
import com.management_system.utilities.core.filter.FilterOption;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.request.FilterRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Facility", description = "Operations related to managing facilities of the store")
@RestController
@RequestMapping(value = "/authen/facility", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class FacilityController {
    final UseCaseExecutor useCaseExecutor;
    final AddNewFacilityUseCase addNewFacilityUseCase;
    final EditFacilityUseCase editFacilityUseCase;
    final FilterFacilitiesUseCase filterFacilitiesUseCase;
    final ViewFacilityDetailsByIdUseCase viewFacilityDetailsByIdUseCase;

    @Operation(summary = "Add one or multiple facilities")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PostMapping("/addNewFacilities")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewFacilities(
            @RequestBody
            List<Facility> facilities
    ) {
        return useCaseExecutor.execute(
                addNewFacilityUseCase,
                new AddNewFacilityUseCase.InputValue(facilities),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Edit a selected facility")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PostMapping("/editFacility")
    public CompletableFuture<ResponseEntity<ApiResponse>> editFacility(
            @RequestBody
            FacilityRequest facilityRequest
    ) {
        return useCaseExecutor.execute(
                editFacilityUseCase,
                new EditFacilityUseCase.InputValue(facilityRequest),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Get facilities by filters")
    @PostMapping("/filterFacilities")
    public CompletableFuture<ResponseEntity<ApiResponse>> filterFacilities(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Filter for facility")
            @RequestBody
            FacilityFilterRequest filterRequest
    ) {
        return useCaseExecutor.execute(
                filterFacilitiesUseCase,
                new FilterFacilitiesUseCase.InputValue(filterRequest),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Get facility's details by ID")
    @GetMapping("/facilityId={id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> viewFacilityById(@PathVariable("id") String id) {
        return useCaseExecutor.execute(
                viewFacilityDetailsByIdUseCase,
                new ViewFacilityDetailsByIdUseCase.InputValue(id),
                ResponseMapper::map
        );
    }
}
