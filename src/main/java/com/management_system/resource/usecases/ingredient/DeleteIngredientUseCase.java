package com.management_system.resource.usecases.ingredient;

import com.management_system.resource.entities.database.ingredient.Ingredient;
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
public class DeleteIngredientUseCase extends UseCase<DeleteIngredientUseCase.InputValue, ApiResponse> {
    private final MongoTemplate mongoTemplate;
    private final RedisServiceClient redisServiceClient;

    public DeleteIngredientUseCase(MongoTemplate mongoTemplate, RedisServiceClient redisServiceClient) {
        this.mongoTemplate = mongoTemplate;
        this.redisServiceClient = redisServiceClient;
    }

    @Override
    public ApiResponse execute(InputValue input) {
        String id = input.ingredientId();
        List<String> errors = new ArrayList<>();
        Query query = new Query(Criteria.where("_id").is(id));
        DeleteResult result = mongoTemplate.remove(query, Ingredient.class);

        if(result.getDeletedCount() > 0) {
            CompletableFuture.runAsync(() -> {
                redisServiceClient.delete(TableName.INGREDIENT, id, true);
            }).exceptionally(e -> {
                e.printStackTrace();

                return null;
            });

            return ApiResponse.builder()
                    .result(ResponseResult.success.name())
                    .errors(errors)
                    .message("Delete ingredient successfully")
                    .status(HttpStatus.OK)
                    .build();
        }
        else {
            errors.add("Ingredient with ID " + id + " does not exist");

            return ApiResponse.builder()
                    .result(ResponseResult.failed.name())
                    .errors(errors)
                    .message("Delete ingredient failed")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public record InputValue(String ingredientId) implements UseCase.InputValue {
    }
}
