package com.management_system.resource.usecases.facility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.entities.database.facility.Facility;
import com.management_system.resource.infrastucture.feign.redis.RedisServiceClient;
import com.management_system.resource.infrastucture.repository.FacilityRepository;
import com.management_system.utilities.constant.enumuration.FilterType;
import com.management_system.utilities.core.redis.RedisRequest;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class ViewFacilityDetailsByIdUseCase extends UseCase<ViewFacilityDetailsByIdUseCase.InputValue, ApiResponse> {
    @Autowired
    FacilityRepository facilityRepo;

    @Autowired
    RedisServiceClient redisServiceClient;

    @Override
    public ApiResponse execute(InputValue input) {
        String facilityId = input.id();
        ApiResponse redisRes;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            redisRes = redisServiceClient.findByKey(FilterType.FACILITY.name(), facilityId);
        } catch (Exception e) {
            e.printStackTrace();
            redisRes = ApiResponse.builder()
                    .result("failed")
                    .message("Error!")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        Object contentObj = redisRes.getContent();
        HttpStatus status = redisRes.getStatus();

        if (status.equals(HttpStatus.OK) && contentObj != null) {
            return ApiResponse.builder()
                    .result("success")
                    .content(contentObj)
                    .status(HttpStatus.OK)
                    .build();
        } else {
            Optional<Facility> facilityOptional = facilityRepo.findById(facilityId);

            if (facilityOptional.isPresent()) {
                CompletableFuture.runAsync(() -> {
                    try {
                        redisServiceClient.save(objectMapper.writeValueAsString(
                                RedisRequest.builder()
                                        .type(FilterType.FACILITY)
                                        .data(objectMapper.convertValue(facilityOptional.get(), Map.class))
                                        .build()
                        ));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });

                return ApiResponse.builder()
                        .result("success")
                        .content(facilityOptional.get())
                        .status(HttpStatus.OK)
                        .build();
            } else {
                return ApiResponse.builder()
                        .result("failed")
                        .content("This facility does not exist")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
        }
    }

    public record InputValue(String id) implements UseCase.InputValue {
    }
}
