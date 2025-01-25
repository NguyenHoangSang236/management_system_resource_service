package com.management_system.resource.usecases.supplier;

import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.resource.entities.request_dto.SupplierRequest;
import com.management_system.resource.infrastucture.feign.redis.RedisServiceClient;
import com.management_system.resource.infrastucture.repository.SupplierRepository;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class EditSupplierUseCase extends UseCase<EditSupplierUseCase.InputValue, ApiResponse> {
    @Autowired
    SupplierRepository supplierRepo;

    @Autowired
    RedisServiceClient redisServiceClient;

    @Autowired
    DbUtils dbUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        if (input.supplierRequest().getId().isBlank()) {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Can not find id field")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        Optional<Supplier> supplierOptional = supplierRepo.findById(input.supplierRequest().getId());

        if (supplierOptional.isPresent()) {
            supplierRepo.save(dbUtils.mergeMongoEntityFromRequest(supplierOptional.get(), input.supplierRequest()));

            CompletableFuture.runAsync(() -> redisServiceClient.delete(
                            TableName.SUPPLIER, input.supplierRequest().getId()))
                    .exceptionally(
                            ex -> {
                                ex.printStackTrace();
                                return null;
                            }
                    );

            return ApiResponse.builder()
                    .result(ResponseResult.success.name())
                    .message("Edit supplier ID " + input.supplierRequest().getId() + " successfully")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Supplier with id " + input.supplierRequest().getId() + " does not exist")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public record InputValue(SupplierRequest supplierRequest) implements UseCase.InputValue {
    }
}
