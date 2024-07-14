package com.management_system.ingredient.usecases.ingredient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.ingredient.entities.database.Ingredient;
import com.management_system.ingredient.infrastucture.feign.RedisServiceClient;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.FilterRequest;
import com.management_system.utilities.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ViewIngredientDetailsByIdUseCase extends UseCase<ViewIngredientDetailsByIdUseCase.InputValue, ApiResponse>{
    @Autowired
    DbUtils dbUtils;

    @Autowired
    RedisServiceClient redisServiceClient;


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            String ingredientId = input.id();

            CompletableFuture<Ingredient> redisFuture = CompletableFuture.supplyAsync(() -> {
                ApiResponse redisRes = redisServiceClient.findByKey("INGREDIENT:" + ingredientId);

                return (Ingredient) redisRes.getContent();
            });

            CompletableFuture<Ingredient> mongoFuture = CompletableFuture.supplyAsync(() -> dbUtils.getDataById(input.id(), Ingredient.class));

            CompletableFuture<Object> res = CompletableFuture.anyOf(redisFuture, mongoFuture);

            Ingredient ingredient = (Ingredient) res.get();

            return ApiResponse.builder()
                    .result("success")
                    .content(ingredient)
                    .status(HttpStatus.OK)
                    .build();
        }
        catch (Exception e) {
            e.printStackTrace();

            return ApiResponse.builder()
                    .result("failed")
                    .content(e.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


    public record InputValue(String id) implements UseCase.InputValue {}
}
