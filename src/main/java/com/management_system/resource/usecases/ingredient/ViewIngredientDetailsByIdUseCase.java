package com.management_system.resource.usecases.ingredient;

import com.management_system.resource.common.caching.MongoRedisClientServiceImpl;
import com.management_system.resource.entities.database.ingredient.Ingredient;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewIngredientDetailsByIdUseCase extends UseCase<ViewIngredientDetailsByIdUseCase.InputValue, ApiResponse> {
    final MongoRedisClientServiceImpl mongoRedisClientService;


    @Override
    public ApiResponse execute(InputValue input) {
        String ingredientId = input.id();

        Ingredient ingredient = (Ingredient) mongoRedisClientService.getAndCacheDataFromOneTable(
                Ingredient.class,
                ingredientId,
                null
        );

        return ApiResponse.builder()
                .result("success")
                .content(ingredient)
                .status(HttpStatus.OK)
                .build();
    }


    public record InputValue(String id) implements UseCase.InputValue {
    }
}
