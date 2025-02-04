package com.management_system.resource.usecases.supplier;

import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.resource.entities.request_dto.supplier.EditSupplierRequest;
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

import java.util.ArrayList;
import java.util.List;
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
        String supplierId = input.editSupplierRequest().getId();
        List<String> errors = new ArrayList<>();

        Optional<Supplier> supplierOptional = supplierRepo.findById(supplierId);

        if (supplierOptional.isPresent()) {
            Supplier supplier = dbUtils.mergeMongoEntityFromRequest(supplierOptional.get(), input.editSupplierRequest());
            supplierRepo.save(supplier);

            CompletableFuture.runAsync(() -> redisServiceClient.delete(TableName.SUPPLIER, supplierId, true))
                    .exceptionally(
                            ex -> {
                                ex.printStackTrace();
                                return null;
                            }
                    );

            return ApiResponse.builder()
                    .result(ResponseResult.success.name())
                    .errors(errors)
                    .message("Edit supplier ID " + input.editSupplierRequest().getId() + " successfully")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            errors.add("Supplier with id " + input.editSupplierRequest().getId() + " does not exist");

            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .message("Edit supplier failed")
                    .errors(errors)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public record InputValue(EditSupplierRequest editSupplierRequest) implements UseCase.InputValue {
    }
}
