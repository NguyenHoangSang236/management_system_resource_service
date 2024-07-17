package com.management_system.ingredient.usecases.ingredient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.ingredient.entities.database.Ingredient;
import com.management_system.ingredient.infrastucture.feign.RedisServiceClient;
import com.management_system.ingredient.infrastucture.repository.IngredientRepository;
import com.management_system.utilities.constant.enumuration.FilterType;
import com.management_system.utilities.core.redis.RedisData;
import com.management_system.utilities.core.redis.RedisRequest;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.FilterRequest;
import com.management_system.utilities.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ViewIngredientDetailsByIdUseCase extends UseCase<ViewIngredientDetailsByIdUseCase.InputValue, ApiResponse>{
    @Autowired
    IngredientRepository ingredientRepo;

    @Autowired
    RedisServiceClient redisServiceClient;


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            String ingredientId = input.id();
            ObjectMapper objectMapper = new ObjectMapper();

            ApiResponse redisRes = redisServiceClient.findByKey("INGREDIENT:" + ingredientId);
            Object contentObj = redisRes.getContent();
            HttpStatus status = redisRes.getStatus();

            if(!status.equals(HttpStatus.NO_CONTENT) && contentObj != null) {
                return ApiResponse.builder()
                        .result("success")
                        .content(contentObj)
                        .status(HttpStatus.OK)
                        .build();
            }
            else {
                Ingredient ingredient = ingredientRepo.getIngredientById(ingredientId);

                if(ingredient != null) {
                    CompletableFuture.runAsync(() -> {
                        try {
                            redisServiceClient.save(objectMapper.writeValueAsString(
                                    RedisRequest
                                            .builder()
                                            .type(FilterType.INGREDIENT)
                                            .data(objectMapper.convertValue(ingredient, Map.class))
                                            .build()
                            ));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }).exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    });;
                }

                return ApiResponse.builder()
                        .result("success")
                        .content(ingredient)
                        .status(HttpStatus.OK)
                        .build();
            }
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
