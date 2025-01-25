package com.management_system.resource.usecases.supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.resource.infrastucture.feign.redis.RedisServiceClient;
import com.management_system.resource.infrastucture.repository.SupplierRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.constant.enumuration.TableName;
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
public class ViewSupplierDetailsByIdUseCase extends UseCase<ViewSupplierDetailsByIdUseCase.InputValue, ApiResponse> {
    @Autowired
    SupplierRepository supplierRepo;

    @Autowired
    RedisServiceClient redisServiceClient;

    @Override
    public ApiResponse execute(InputValue input) {
        String supplierId = input.id();
        ApiResponse redisRes;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            redisRes = redisServiceClient.find(TableName.SUPPLIER, supplierId);
        } catch (Exception e) {
            e.printStackTrace();
            redisRes = ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Error!")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        Object contentObj = redisRes.getContent();
        HttpStatus status = redisRes.getStatus();

        if (status.equals(HttpStatus.OK) && contentObj != null) {
            return ApiResponse.builder()
                    .result(ResponseResult.success.name())
                    .content(contentObj)
                    .status(HttpStatus.OK)
                    .build();
        } else {
            Optional<Supplier> supplierOptional = supplierRepo.findById(supplierId);

            if (supplierOptional.isPresent()) {
                CompletableFuture.runAsync(() -> {
                    try {
                        redisServiceClient.save(objectMapper.writeValueAsString(
                                RedisRequest.builder()
                                        .type(TableName.SUPPLIER)
                                        .data(objectMapper.convertValue(supplierOptional.get(), Map.class))
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
                        .result(ResponseResult.success.name())
                        .content(supplierOptional.get())
                        .status(HttpStatus.OK)
                        .build();
            } else {
                return ApiResponse.builder()
                        .result(ResponseResult.failed.name())
                        .content("This supplier does not exist")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
        }
    }

    public record InputValue(String id) implements UseCase.InputValue {
    }
}
