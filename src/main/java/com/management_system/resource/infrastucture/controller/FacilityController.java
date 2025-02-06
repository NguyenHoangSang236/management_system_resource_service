package com.management_system.resource.infrastucture.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.entities.request_dto.facility.AddFacilityRequest;
import com.management_system.resource.entities.request_dto.filter_requests.FacilityFilterRequest;
import com.management_system.resource.entities.request_dto.facility.EditFacilityRequest;
import com.management_system.resource.usecases.facility.*;
import com.management_system.resource.usecases.supplier.DeleteSupplierUseCase;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    final DeleteFacilityUseCase deleteFacilityUseCase;
    final ViewFacilityDetailsByIdUseCase viewFacilityDetailsByIdUseCase;

    @Operation(summary = "Add new facility")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewFacilities(
            @Valid
            @RequestPart("data")
            @NotNull
            String facilityRequestBody,
            @NotNull
            @Parameter(description = "File to upload", required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @ImageFileValid
            @RequestPart("image")
            MultipartFile image
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        AddFacilityRequest facilityRequest = objectMapper.readValue(facilityRequestBody, AddFacilityRequest.class);

        return useCaseExecutor.execute(
                addNewFacilityUseCase,
                new AddNewFacilityUseCase.InputValue(facilityRequest, image),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Edit a selected facility")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @PatchMapping("/edit")
    public CompletableFuture<ResponseEntity<ApiResponse>> editFacility(
            @Valid
            @RequestPart("data")
            @NotNull
            String facilityRequestBody,
            @Parameter(description = "File to upload", required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @ImageFileValid(nullable = true)
            @RequestPart("image")
            MultipartFile image
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        EditFacilityRequest facilityRequest = objectMapper.readValue(facilityRequestBody, EditFacilityRequest.class);

        return useCaseExecutor.execute(
                editFacilityUseCase,
                new EditFacilityUseCase.InputValue(facilityRequest, image),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Get facilities by filters")
    @PostMapping("/filter")
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
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> viewFacilityById(@PathVariable("id") String id) {
        return useCaseExecutor.execute(
                viewFacilityDetailsByIdUseCase,
                new ViewFacilityDetailsByIdUseCase.InputValue(id),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Delete facility by ID")
    @PreAuthorize(ConstantValue.MANAGER_AUTHOR)
    @DeleteMapping("/delete/{id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> deleteFacilityById(@PathVariable("id") String id) {
        return useCaseExecutor.execute(
                deleteFacilityUseCase,
                new DeleteFacilityUseCase.InputValue(id),
                ResponseMapper::map
        );
    }
}
