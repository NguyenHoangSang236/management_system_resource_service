package com.management_system.resource.infrastucture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.management_system.resource.entities.database.facility.Facility;
import com.management_system.resource.entities.request_dto.FacilityFilterOptions;
import com.management_system.resource.entities.request_dto.FacilityRequest;
import com.management_system.resource.usecases.facility.AddNewFacilityUseCase;
import com.management_system.resource.usecases.facility.EditFacilityUseCase;
import com.management_system.resource.usecases.facility.FilterFacilitiesUseCase;
import com.management_system.resource.usecases.facility.ViewFacilityDetailsByIdUseCase;
import com.management_system.utilities.core.deserializer.FilterOptionsDeserializer;
import com.management_system.utilities.core.filter.FilterOption;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.request.FilterRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/authen/facility", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class FacilityController {
    final UseCaseExecutor useCaseExecutor;
    final AddNewFacilityUseCase addNewFacilityUseCase;
    final EditFacilityUseCase editFacilityUseCase;
    final FilterFacilitiesUseCase filterFacilitiesUseCase;
    final ViewFacilityDetailsByIdUseCase viewFacilityDetailsByIdUseCase;


    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/addNewFacilities")
    public CompletableFuture<ResponseEntity<ApiResponse>> addNewFacilities(@RequestBody List<Facility> facilities) {
        return useCaseExecutor.execute(
                addNewFacilityUseCase,
                new AddNewFacilityUseCase.InputValue(facilities),
                ResponseMapper::map
        );
    }


    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/editFacility")
    public CompletableFuture<ResponseEntity<ApiResponse>> editFacility(@RequestBody FacilityRequest facilityRequest) {
        return useCaseExecutor.execute(
                editFacilityUseCase,
                new EditFacilityUseCase.InputValue(facilityRequest),
                ResponseMapper::map
        );
    }


    @PostMapping("/filterFacilities")
    public CompletableFuture<ResponseEntity<ApiResponse>> filterFacilities(@RequestBody String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        module.addDeserializer(FilterOption.class, new FilterOptionsDeserializer(FacilityFilterOptions.class));
        objectMapper.registerModule(module);

        FilterRequest filterRequest = objectMapper.readValue(json, FilterRequest.class);

        return useCaseExecutor.execute(
                filterFacilitiesUseCase,
                new FilterFacilitiesUseCase.InputValue(filterRequest),
                ResponseMapper::map
        );
    }


    @GetMapping("/facilityId={id}")
    public CompletableFuture<ResponseEntity<ApiResponse>> viewFacilityById(@PathVariable("id") String id) {
        return useCaseExecutor.execute(
                viewFacilityDetailsByIdUseCase,
                new ViewFacilityDetailsByIdUseCase.InputValue(id),
                ResponseMapper::map
        );
    }
}
