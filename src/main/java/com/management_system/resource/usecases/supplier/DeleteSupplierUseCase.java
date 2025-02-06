package com.management_system.resource.usecases.supplier;

import com.management_system.resource.entities.database.supplier.Supplier;
import com.management_system.resource.infrastucture.feign.redis.RedisServiceClient;
import com.management_system.utilities.constant.enumuration.ResponseResult;
import com.management_system.utilities.constant.enumuration.TableName;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class DeleteSupplierUseCase extends UseCase<DeleteSupplierUseCase.InputValue, ApiResponse> {
    private final RedisServiceClient redisServiceClient;
    private final MongoTemplate mongoTemplate;

    public DeleteSupplierUseCase(RedisServiceClient redisServiceClient, MongoTemplate mongoTemplate) {
        this.redisServiceClient = redisServiceClient;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ApiResponse execute(InputValue input) {
        String id = input.supplierId();
        List<String> errors = new ArrayList<>();

        Query query = new Query(Criteria.where("_id").is(id));
        DeleteResult result = mongoTemplate.remove(query, Supplier.class);

        if(result.getDeletedCount() > 0) {
            CompletableFuture.runAsync(() -> {
                redisServiceClient.delete(TableName.SUPPLIER, id, true);
            }).exceptionally(e -> {
                e.printStackTrace();

                return null;
            });

            return ApiResponse.builder()
                    .result(ResponseResult.success.name())
                    .errors(errors)
                    .message("Delete supplier successfully")
                    .status(HttpStatus.OK)
                    .build();
        }
        else {
            errors.add("Supplier with ID " + id + " does not exist");

            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .errors(errors)
                    .message("Delete supplier failed")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public record InputValue(String supplierId) implements UseCase.InputValue {
    }
}
